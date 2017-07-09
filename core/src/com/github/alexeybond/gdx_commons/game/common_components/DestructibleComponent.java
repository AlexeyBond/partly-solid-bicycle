package com.github.alexeybond.gdx_commons.game.common_components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Pool;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.declarative.EntityDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.visitor.impl.ApplyEntityDeclarationVisitor;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.FixturePhysicsComponent;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.FixtureDefFixtureComponent;
import com.github.alexeybond.gdx_commons.game.systems.render.components.PolySpriteComponent;
import com.github.alexeybond.gdx_commons.game.utils.destruction.Destroyer;
import com.github.alexeybond.gdx_commons.game.utils.destruction.DestroyerConfig;
import com.github.alexeybond.gdx_commons.game.utils.destruction.DestructionHelper;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DestructibleComponent implements Component {
    private final ApplyEntityDeclarationVisitor entityDeclarationVisitor = new ApplyEntityDeclarationVisitor();

    private final String destructionEndEventName;
    private final String centerDestructionStartEventName;
    private final ShapeSource shapeSource;
    private final EntityDeclaration partClass;
    private final GameDeclaration gameDeclaration;
    private final float partDensity, partRestitution, partFriction;
    private final TextureRegion textureRegion;
    private final float[] texturePlacement;
    private final String partPass;
    private final DestroyerConfig destroyerConfig;
    private final Pool<Destroyer> destroyerPool;

    private int lifeNumber = 0;

    private Entity entity;
    private Event<Component> centerDestructionStartEvent, destructionEndEvent;
    private int centerDestructionStartEventSubIdx = -1;
    private Vec2Property<Component> entityPosition;
    private FloatProperty<Component> entityRotation;

    private boolean destructionInProgress;
    private Destroyer destroyer;

    public DestructibleComponent(
            String destructionEndEventName,
            String centerDestructionStartEventName,
            ShapeSource shapeSource,
            EntityDeclaration partClass,
            GameDeclaration gameDeclaration,
            float partDensity, float partRestitution, float partFriction,
            TextureRegion textureRegion,
            float[] texturePlacement,
            String partPass, DestroyerConfig destroyerConfig,
            Pool<Destroyer> destroyerPool) {
        this.destructionEndEventName = destructionEndEventName;
        this.centerDestructionStartEventName = centerDestructionStartEventName;
        this.shapeSource = shapeSource;
        this.partClass = partClass;
        this.gameDeclaration = gameDeclaration;
        this.partDensity = partDensity;
        this.partRestitution = partRestitution;
        this.partFriction = partFriction;
        this.textureRegion = textureRegion;
        this.texturePlacement = texturePlacement;
        this.partPass = partPass;
        this.destroyerConfig = destroyerConfig;
        this.destroyerPool = destroyerPool;
    }

    @Override
    public void onConnect(Entity entity) {
        this.entity = entity;

        entityPosition = entity.events().event("position", Vec2Property.<Component>make());
        entityRotation = entity.events().event("rotation", FloatProperty.<Component>make());
        destructionEndEvent = entity.events().event(destructionEndEventName, Event.<Component>make());

        if (null != centerDestructionStartEventName) {
            this.centerDestructionStartEvent = entity.events()
                    .event(centerDestructionStartEventName, Event.<Component>make());
            this.centerDestructionStartEventSubIdx = centerDestructionStartEvent.subscribe(
                    new EventListener<Component, Event<Component>>() {
                        @Override
                        public boolean onTriggered(Component component, Event<Component> event) {
                            startCenterDestruction();
                            return true;
                        }
                    }
            );
        }

        destructionInProgress = false;
        ++lifeNumber;
    }

    @Override
    public void onDisconnect(Entity entity) {
        if (null != centerDestructionStartEvent) {
            this.centerDestructionStartEventSubIdx = centerDestructionStartEvent
                    .unsubscribe(centerDestructionStartEventSubIdx);
            this.centerDestructionStartEvent = null;
        }

        ++lifeNumber;
    }

    public void startCenterDestruction() {
        if (destructionInProgress) return;

        prepareDestruction();
        destroyer.startCenter();
        computeDestruction();
    }

    private void prepareDestruction() {
        destroyer = destroyerPool.obtain();
        destroyer.configure(destroyerConfig);
        destroyer.prepare(shapeSource.getShape(entity));
        destructionInProgress = true;
    }

    private void computeDestruction() {
        if (shouldRunAsync()) {
            final int lifeNumberPrev = lifeNumber;
            new Thread() {
                @Override
                public void run() {
                    final ArrayList<ArrayList<Vector2>> parts = destroyer.compute();

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            if (lifeNumber == lifeNumberPrev) {
                                processDestructionResult(parts);
                            }
                        }
                    });
                }
            }.start();
        } else {
            processDestructionResult(destroyer.compute());
        }
    }

    protected boolean shouldRunAsync() {
        return false;
    }

    private void processDestructionResult(ArrayList<ArrayList<Vector2>> parts) {
        DestructionHelper destructionHelper = new DestructionHelper();

        try {
            destructionHelper.setupTexturePlacement(textureRegion, texturePlacement);

            for (int i = 0; i < parts.size(); i++) {
                spawnPart(parts.get(i), destructionHelper);
            }
        } finally {
            destructionHelper.dispose();
        }

        destroyerPool.free(destroyer);
        destroyer = null;
        destructionInProgress = false;
        destructionEndEvent.trigger(this);
    }

    private void spawnPart(ArrayList<Vector2> shape, DestructionHelper destructionHelper) {
        destructionHelper.beginPart(shape);

        Entity partEntity = new Entity(entity.game());
        partEntity = entityDeclarationVisitor.doVisit(partClass, gameDeclaration, partEntity);

        FixtureDef fd;

        while (null != (fd = destructionHelper.nextFixture())) {
            fd.density = partDensity;
            fd.friction = partFriction;
            fd.restitution = partRestitution;

            partEntity.components().add(destructionHelper.fixtureName(),
                    new FixtureDefFixtureComponent(fd));
        }

        partEntity.components().add("sprite",
                new PolySpriteComponent(
                        partPass,
                        destructionHelper.partRenderVertices(),
                        destructionHelper.partRenderIndices(),
                        textureRegion.getTexture()
                ));

        Vec2Property<Component> partPosition = partEntity.events()
                .event("position", Vec2Property.<Component>make());
        FloatProperty<Component> partRotation = partEntity.events()
                .event("rotation", FloatProperty.<Component>make());

        partRotation.set(this, entityRotation.get());
        partPosition.set(this, partPosition.ref()
                .set(destructionHelper.center()).rotate(entityRotation.get())
                .add(entityPosition.ref()));
    }

    //

    public interface ShapeSource {
        List<Vector2> getShape(Entity entity);
    }

    public static class FixedShapeSource implements ShapeSource {
        private final List<Vector2> shape;

        public FixedShapeSource(List<Vector2> shape) {
            this.shape = shape;
        }

        @Override
        public List<Vector2> getShape(Entity entity) {
            return shape;
        }
    }

    public static class FixtureShapeSource implements ShapeSource {
        private final String fixtureName;

        public FixtureShapeSource(String fixtureName) {
            this.fixtureName = fixtureName;
        }

        @Override
        public List<Vector2> getShape(Entity entity) {
            FixturePhysicsComponent fixtureComponent = entity.components().get(fixtureName);
            PolygonShape polygonShape = (PolygonShape)fixtureComponent.fixture().getShape();

            int c = polygonShape.getVertexCount();
            ArrayList<Vector2> vertices = new ArrayList<Vector2>(c);

            for (int i = 0; i < c; i++) {
                Vector2 vert = new Vector2();
                polygonShape.getVertex(i, vert);
                vertices.add(vert);
            }

            return vertices;
        }
    }
}
