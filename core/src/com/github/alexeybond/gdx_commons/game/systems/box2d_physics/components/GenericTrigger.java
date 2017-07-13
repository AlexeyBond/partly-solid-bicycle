package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.CollisionData;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;

import java.util.NoSuchElementException;

/**
 * Component that triggers an event on entity colliding component's owner passing owner entity as
 * event parameter if event has type of {@link ObjectProperty} (otherwise just triggers the event).
 */
public class GenericTrigger
        implements Component, EventListener<ObjectProperty<CollisionData>> {
    private final String ownerCollisionEventName, targetEventName;

    private ObjectProperty<CollisionData> hitEvent;
    private int hitSubIdx = -1;
    private Entity entity;

    public GenericTrigger(String ownerCollisionEventName, String targetEventName) {
        this.ownerCollisionEventName = ownerCollisionEventName;
        this.targetEventName = targetEventName;
    }

    @Override
    public void onConnect(Entity entity) {
        this.entity = entity;
        hitEvent = entity.events()
                .event(ownerCollisionEventName, ObjectProperty.<CollisionData>make());
        hitSubIdx = hitEvent.subscribe(this);
    }

    @Override
    public void onDisconnect(Entity entity) {
        hitSubIdx = hitEvent.unsubscribe(hitSubIdx);
    }

    @Override
    public boolean onTriggered(ObjectProperty<CollisionData> event) {
        Entity target = event.get().that.entity();

        Event targetEvent;

        try {
            targetEvent = target.events().event(targetEventName);
        } catch (NoSuchElementException e) {
            return false;
        }

        if (targetEvent instanceof ObjectProperty) {
            ObjectProperty<Entity> asObjectProperty = (ObjectProperty) targetEvent;

            asObjectProperty.setSilently(entity);
            asObjectProperty.trigger();
        } else {
            targetEvent.trigger();
        }

        return true;
    }
}
