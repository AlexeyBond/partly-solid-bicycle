package com.github.alexeybond.partly_solid_bicycle.ext.destruction.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.EntityDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.visitor.impl.ApplyEntityDeclarationVisitor;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.interfaces.APhysicsSystem;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.interfaces.FixturePhysicsComponent;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.components.FixtureDefFixtureComponent;
import com.github.alexeybond.partly_solid_bicycle.ext.destruction.Destroyer;
import com.github.alexeybond.partly_solid_bicycle.ext.destruction.DestroyerConfig;
import com.github.alexeybond.partly_solid_bicycle.ext.destruction.DestructionHelper;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.decl.PolySpriteComponentDecl;
import com.github.alexeybond.partly_solid_bicycle.util.event.Event;
import com.github.alexeybond.partly_solid_bicycle.util.event.EventListener;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.FloatProperty;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.Vec2Property;
import com.github.alexeybond.partly_solid_bicycle.util.interfaces.Creatable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DestructibleComponent implements Component {
    private final static String[] SPRITE_NAMES = {
            "sprite",
            "sprite-0",
            "sprite-1",
            "sprite-2",
    };

    private final ApplyEntityDeclarationVisitor entityDeclarationVisitor = new ApplyEntityDeclarationVisitor();

    private final String destructionEndEventName;
    private final String centerDestructionStartEventName;
    private final ShapeSource shapeSource;
    private final EntityDeclaration partClass;
    private final GameDeclaration gameDeclaration;
    private final float partDensity, partRestitution, partFriction;
    private final TextureRegion textureRegion;
    private final String regionName;
    private final PolySpriteComponentDecl[] spriteDeclarations;
    private final float[] texturePlacement;
    private final String partPass;
    private final DestroyerConfig destroyerConfig;
    private final Pool<Destroyer> destroyerPool;

    private int lifeNumber = 0;

    private APhysicsSystem physicsSystem;
    private Entity entity;
    private Event centerDestructionStartEvent, destructionEndEvent;
    private int centerDestructionStartEventSubIdx = -1;
    private Vec2Property entityPosition;
    private FloatProperty entityRotation;

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
            String regionName, PolySpriteComponentDecl[] spriteDeclarations, float[] texturePlacement,
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
        this.regionName = regionName;
        this.spriteDeclarations = spriteDeclarations;
        this.texturePlacement = texturePlacement;
        this.partPass = partPass;
        this.destroyerConfig = destroyerConfig;
        this.destroyerPool = destroyerPool;
    }

    @Override
    public void onConnect(Entity entity) {
        this.entity = entity;

        physicsSystem = entity.game().systems().get("physics");

        entityPosition = entity.events().event("position", Vec2Property.make());
        entityRotation = entity.events().event("rotation", FloatProperty.make());
        destructionEndEvent = entity.events().event(destructionEndEventName, Event.makeEvent());

        if (null != centerDestructionStartEventName) {
            this.centerDestructionStartEvent = entity.events()
                    .event(centerDestructionStartEventName, Event.makeEvent());
            this.centerDestructionStartEventSubIdx = centerDestructionStartEvent.subscribe(
                    new EventListener<Event>() {
                        @Override
                        public boolean onTriggered(Event event) {
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

    private class PartsCreatable implements Creatable {
        private DestructionHelper destructionHelper;
        private Destroyer destroyer;
        private ArrayList<ArrayList<Vector2>> parts;

        private PartsCreatable(DestructionHelper destructionHelper, Destroyer destroyer, ArrayList<ArrayList<Vector2>> parts) {
            this.destructionHelper = destructionHelper;
            this.destroyer = destroyer;
            this.parts = parts;
        }

        @Override
        public void create() {
            try {
                for (int i = 0; i < parts.size(); i++) {
                    spawnPart(parts.get(i), destructionHelper);
                }
            } finally {
                destructionHelper.dispose();
                destructionHelper = null;
            }

            destroyerPool.free(destroyer);
            destructionInProgress = false;
            destructionEndEvent.trigger();
        }
    }

    private void processDestructionResult(ArrayList<ArrayList<Vector2>> parts) {
        DestructionHelper destructionHelper = new DestructionHelper();

        destructionHelper.setupTexturePlacement(textureRegion, texturePlacement);

        physicsSystem.enqueueCreatable(new PartsCreatable(destructionHelper, destroyer, parts));

        destroyer = null;
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

        float[] vertices = destructionHelper.partRenderVertices();
        short[] triangles = destructionHelper.partRenderIndices();

        for (int i = 0; i < spriteDeclarations.length; i++) {
            spriteDeclarations[i].vertices = vertices;
            spriteDeclarations[i].triangles = triangles;
            String origTexture = spriteDeclarations[i].texture;
            if (null == origTexture) {
                spriteDeclarations[i].texture = regionName;
            }
            partEntity.components().add(SPRITE_NAMES[i],
                    spriteDeclarations[i].create(gameDeclaration, entity.game()));
            spriteDeclarations[i].texture = origTexture;
        }

        Vec2Property partPosition = partEntity.events()
                .event("position", Vec2Property.make());
        FloatProperty partRotation = partEntity.events()
                .event("rotation", FloatProperty.make());

        partRotation.set(entityRotation.get());
        partPosition.set(partPosition.ref()
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
