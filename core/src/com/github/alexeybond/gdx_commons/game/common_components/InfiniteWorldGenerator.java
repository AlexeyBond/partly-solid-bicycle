package com.github.alexeybond.gdx_commons.game.common_components;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.declarative.EntityDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.render.RenderComponent;
import com.github.alexeybond.gdx_commons.game.systems.render.RenderSystem;
import com.github.alexeybond.gdx_commons.game.systems.tagging.TagGroup;
import com.github.alexeybond.gdx_commons.game.systems.tagging.TaggingSystem;
import com.github.alexeybond.gdx_commons.game.systems.tagging.components.SingleTagComponent;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.IntProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;

/**
 *
 */
public class InfiniteWorldGenerator
        implements Component, EventListener<Component, Vec2Property<Component>>, RenderComponent {
    private final String observerTag, generatedTag;
    private final Array<EntityDeclaration> generateClasses;
    private final GameDeclaration gameDeclaration;

    private IntProperty<Component> leaveZoneSizeProp;
    private IntProperty<Component> keepZoneSizeProp;
    private IntProperty<Component> generateZoneSizeProp;
    private FloatProperty<Component> gridSizeProperty;
    private Vec2Property<Component> observerPositionProp;
    private TagGroup observerGroup;
    private TagGroup generatedGroup;
    private Game game;

    private int observerPosSubIdx = -1;

    private final Vector2 roundObserverPos = new Vector2();
    private final Rectangle leaveZone = new Rectangle(0,0,0,0);
    private final Rectangle keepZone = new Rectangle(0,0,0,0);

    private final Array<Entity> removeArray = new Array<Entity>();

    public InfiniteWorldGenerator(
            String observerTag,
            String generatedTag,
            Array<EntityDeclaration> generateClasses,
            GameDeclaration gameDeclaration) {
        this.observerTag = observerTag;
        this.generatedTag = generatedTag;
        this.generateClasses = generateClasses;
        this.gameDeclaration = gameDeclaration;
    }

    @Override
    public void onConnect(Entity entity) {
        leaveZoneSizeProp = entity.events().event("leaveZone", IntProperty.<Component>make(1));
        keepZoneSizeProp = entity.events().event("keepZone", IntProperty.<Component>make(2));
        generateZoneSizeProp = entity.events().event("generateZone", IntProperty.<Component>make(1));
        gridSizeProperty = entity.events().event("gridSize", FloatProperty.<Component>make(100));

        game = entity.game();

        TaggingSystem taggingSystem = game.systems().get("tagging");
        observerGroup = taggingSystem.group(observerTag);
        generatedGroup = taggingSystem.group(generatedTag);

        observerPositionProp = observerGroup.getOnly().events().event("position");

        observerPosSubIdx = observerPositionProp.subscribe(this);

        RenderSystem renderSystem = game.systems().get("render");
        renderSystem.addToPass("game-debug", this);

        regenerate();
    }

    @Override
    public void onDisconnect(Entity entity) {
        observerPosSubIdx = observerPositionProp.unsubscribe(observerPosSubIdx);
    }

    @Override
    public boolean onTriggered(Component component, Vec2Property<Component> event) {
        if (!leaveZone.contains(event.ref())) {
            regenerate();
            return true;
        }

        return false;
    }

    private void removeAllNotIn(Rectangle keepZone) {
        Array<Entity> allGenerated = generatedGroup.getAll();

        for (int i = 0; i < allGenerated.size; i++) {
            Entity entity = allGenerated.get(i);
            Vec2Property<Component> pos = entity.events().event("position");
            if (!keepZone.contains(pos.ref()))
                removeArray.add(entity);
        }

        for (int i = 0; i < removeArray.size; i++) {
            removeArray.get(i).destroy();
        }

        removeArray.clear();
    }

    private void setupZone(Rectangle zone, Vector2 roundPos, float gridSize, int zoneSize) {
        zone.set(roundPos.x - gridSize * zoneSize, roundPos.y - gridSize * zoneSize,
                2f * gridSize * zoneSize, 2f * gridSize * zoneSize);
    }

    private Entity generateEntity() {
        int classIdx = MathUtils.random(generateClasses.size - 1);
        return generateClasses.get(classIdx).apply(new Entity(game), gameDeclaration);
    }

    private void regenerate() {
        float gridSize = gridSizeProperty.get();
        int leaveSize = leaveZoneSizeProp.get();
        int keepSize = keepZoneSizeProp.get();
        int generateSize = generateZoneSizeProp.get();

        Vector2 observerPos = observerPositionProp.ref();
        roundObserverPos.set(
                gridSize * (float)(int) (observerPos.x / gridSize),
                gridSize * (float)(int) (observerPos.y / gridSize));
        setupZone(keepZone, roundObserverPos, gridSize, keepSize);
        setupZone(leaveZone, roundObserverPos, gridSize, leaveSize);
        removeAllNotIn(keepZone);

        for (int gridX = -generateSize; gridX < generateSize; gridX++) {
            float startX = roundObserverPos.x + gridSize * (float) gridX;

            for (int gridY = -generateSize; gridY < generateSize; gridY++) {
                if (((gridX >= -keepSize) && (gridX < keepSize)) &&
                    ((gridY >= -keepSize) && (gridY < keepSize))) continue;

                float startY = roundObserverPos.y + gridSize * (float) gridY;

                float posX = MathUtils.random(startX, startX + gridSize);
                float posY = MathUtils.random(startY, startY + gridSize);

                Entity entity = generateEntity();
                Vec2Property<Component> entityPos = entity.events().event("position");
                entityPos.set(this, posX, posY);

                entity.components().add("generatedEntityTag",
                        new SingleTagComponent(generatedTag));
            }
        }
    }

    @Override
    public void draw(DrawingContext context) {
        ShapeRenderer shapeRenderer = context.state().beginLines();

        shapeRenderer.rect(keepZone.x, keepZone.y, keepZone.width, keepZone.height);
        shapeRenderer.rect(leaveZone.x, leaveZone.y, leaveZone.width, leaveZone.height);
        shapeRenderer.triangle(
                roundObserverPos.x, roundObserverPos.y + 50,
                roundObserverPos.x + 50, roundObserverPos.y - 50,
                roundObserverPos.x - 50, roundObserverPos.y - 50
        );
    }
}
