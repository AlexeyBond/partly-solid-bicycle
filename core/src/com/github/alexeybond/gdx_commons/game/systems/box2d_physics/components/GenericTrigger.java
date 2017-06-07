package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.CollisionData;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;

/**
 * Component that triggers an event on entity colliding component's owner passing owner entity as
 * event parameter.
 */
public class GenericTrigger
        implements Component, EventListener<Component, ObjectProperty<CollisionData, Component>> {
    private final String ownerCollisionEventName, targetEventName;

    private ObjectProperty<CollisionData, Component> hitEvent;
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
                .event(ownerCollisionEventName, ObjectProperty.<CollisionData, Component>make());
        hitSubIdx = hitEvent.subscribe(this);
    }

    @Override
    public void onDisconnect(Entity entity) {
        hitSubIdx = hitEvent.unsubscribe(hitSubIdx);
    }

    @Override
    public boolean onTriggered(Component component, ObjectProperty<CollisionData, Component> event) {
        Entity target = event.get().that.entity();

        ObjectProperty<Entity, Component> targetEvent;

        try {
            targetEvent = target.events().event(targetEventName);
        } catch (Exception e) {
            return false;
        }

        targetEvent.setSilently(entity);
        targetEvent.trigger(this);

        return true;
    }
}
