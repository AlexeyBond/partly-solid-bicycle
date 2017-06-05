package com.github.alexeybond.gdx_gm2.test_game.game.components;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.CollisionData;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;

import java.util.NoSuchElementException;

/**
 * Triggers "playerCollect" event with player entity on entities hitting player's collector trigger
 * ("collectorHitStart" collision event).
 */
public class SpaceshipCollector
        implements Component, EventListener<Component, ObjectProperty<CollisionData, Component>> {
    private int collisionSubIdx = -1;
    private Entity entity;

    @Override
    public void onConnect(Entity entity) {
        this.entity = entity;
        collisionSubIdx = entity.events()
                .event("collectorHitStart", ObjectProperty.<CollisionData, Component>make())
                .subscribe(this);

        entity.events().event("fuelCapacity", FloatProperty.<Component>make(100));
    }

    @Override
    public void onDisconnect(Entity entity) {
        collisionSubIdx = entity.events()
                .event("collectorHitStart", ObjectProperty.<CollisionData, Component>make())
                .unsubscribe(collisionSubIdx);
    }

    @Override
    public boolean onTriggered(Component initiator, ObjectProperty<CollisionData, Component> event) {
        if (event.get().that.entity() == entity) return false;

        ObjectProperty<Entity, Component> playerCollisionEvent;

        try {
            playerCollisionEvent = event.get().that.entity().events().event("playerCollect");
        } catch (NoSuchElementException e) {
            return false;
        }

        playerCollisionEvent.setSilently(entity);
        playerCollisionEvent.trigger(this);

        return true;
    }
}
