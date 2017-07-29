package com.github.alexeybond.partly_solid_bicycle.game.common_components;

import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.EntityDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.TagGroup;
import com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.TaggingSystem;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.Vec2Property;

/**
 *
 */
public class SpawnOnEventNearToObserver extends SpawnOnEvent {
    private final String observerTag;
    private final float maxObserverDistance2;

    private TagGroup observerTagGroup;

    public SpawnOnEventNearToObserver(
            String eventName,
            Vector2 offset,
            float rotation,
            EntityDeclaration[] spawnClasses,
            GameDeclaration gameDeclaration, String observerTag, float maxObserverDistance) {
        super(eventName, offset, rotation, spawnClasses, gameDeclaration);
        this.observerTag = observerTag;
        this.maxObserverDistance2 = maxObserverDistance * maxObserverDistance;
    }

    @Override
    public void onConnect(Entity entity) {
        super.onConnect(entity);

        observerTagGroup = entity.game().systems().<TaggingSystem>get("tagging").group(observerTag);
    }

    @Override
    protected boolean checkSpawn() {
        Vec2Property observerPosition = observerTagGroup.getOnly()
                .events().event("position");
        return observerPosition.ref().dst2(entityPositionProp.ref()) <= maxObserverDistance2;
    }
}
