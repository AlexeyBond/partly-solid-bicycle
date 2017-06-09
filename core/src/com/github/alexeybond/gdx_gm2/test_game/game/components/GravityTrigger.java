package com.github.alexeybond.gdx_gm2.test_game.game.components;

import com.badlogic.gdx.utils.Array;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.CollisionData;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;

/**
 * Component that gravitates all entities touching a trigger of owner entity.
 */
public class GravityTrigger
        implements Component, EventListener<Component, ObjectProperty<CollisionData, Component>> {
    private final String hitBeginEventName, hitEndEventName, attractorName, enableEventName;

    private Entity entity;

    private BooleanProperty<Component> enableEvent;
    private ObjectProperty<CollisionData, Component> hitBeginEvent, hitEndEvent;
    private Vec2Property<Component> ownerPosition;
    private int hitBeginSubId, hitEndSubId;

    private Array<Entity> affectedEntities = new Array<Entity>();

    public GravityTrigger(
            String hitBeginEventName,
            String hitEndEventName,
            String attractorName,
            String enableEventName) {
        this.hitBeginEventName = hitBeginEventName;
        this.hitEndEventName = hitEndEventName;
        this.attractorName = attractorName;
        this.enableEventName = enableEventName;
    }

    @Override
    public void onConnect(Entity entity) {
        this.entity = entity;
        hitBeginEvent = entity.events()
                .event(hitBeginEventName, ObjectProperty.<CollisionData, Component>make());
        hitEndEvent = entity.events()
                .event(hitEndEventName, ObjectProperty.<CollisionData, Component>make());
        ownerPosition = entity.events()
                .event("position", Vec2Property.<Component>make());
        enableEvent = entity.events()
                .event(enableEventName, BooleanProperty.<Component>make(true));
        hitBeginSubId = hitBeginEvent.subscribe(this);
        hitEndSubId = hitEndEvent.subscribe(this);
    }

    @Override
    public void onDisconnect(Entity entity) {
        for (int i = 0; i < affectedEntities.size; i++) {
            affectedEntities.get(i).components().remove(attractorName);
        }

        affectedEntities.clear();

        hitBeginSubId = hitBeginEvent.unsubscribe(hitBeginSubId);
        hitEndSubId = hitEndEvent.unsubscribe(hitEndSubId);
    }

    @Override
    public boolean onTriggered(Component component, ObjectProperty<CollisionData, Component> event) {
        Entity affected = event.get().that.entity();

        if (affected == entity) return false;

        if (event == hitBeginEvent) {
            if (!affected.alive()) return false;

            affected.components().add(attractorName, new GravityAttractionEffect(ownerPosition, enableEvent, 100000000));
            affectedEntities.add(affected);
        } else {
            affected.components().remove(attractorName);
            affectedEntities.removeValue(affected, true);
        }

        return false;
    }
}
