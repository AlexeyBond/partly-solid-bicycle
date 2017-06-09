package com.github.alexeybond.gdx_commons.game.common_components;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.declarative.EntityDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;

/**
 *
 */
public class SpawnOnEvent
        implements Component, EventListener<Component, Event<Component>> {
    private final String eventName;
    private final Vector2 offset;
    private final float rotation;
    private final EntityDeclaration[] spawnClasses;
    private final GameDeclaration gameDeclaration;

    protected Entity entity;
    protected Vec2Property<Component> entityPositionProp;
    protected FloatProperty<Component> entityRotationProp;
    protected Event<Component> event;
    private int eventSubIdx = -1;

    private final Vector2 tmp = new Vector2();

    public SpawnOnEvent(
            String eventName,
            Vector2 offset,
            float rotation,
            EntityDeclaration[] spawnClasses,
            GameDeclaration gameDeclaration) {
        this.eventName = eventName;
        this.offset = offset;
        this.rotation = rotation;
        this.spawnClasses = spawnClasses;
        this.gameDeclaration = gameDeclaration;
    }

    @Override
    public void onConnect(Entity entity) {
        this.entity = entity;
        event = entity.events().event(eventName);
        eventSubIdx = event.subscribe(this);

        entityPositionProp = entity.events().event("position", Vec2Property.<Component>make());
        entityRotationProp = entity.events().event("rotation", FloatProperty.<Component>make());
    }

    @Override
    public void onDisconnect(Entity entity) {
        eventSubIdx = event.unsubscribe(eventSubIdx);
    }

    @Override
    public boolean onTriggered(Component component, Event<Component> event) {
        if (!checkSpawn()) return false;

        EntityDeclaration cls = spawnClasses[MathUtils.random(0, spawnClasses.length - 1)];
        Entity spawned = cls.apply(new Entity(entity.game()), gameDeclaration);

        tmp.set(offset).rotate(entityRotationProp.get()).add(entityPositionProp.ref());

        Vec2Property<Component> spawnedPositionProp = spawned.events()
                .event("position", Vec2Property.<Component>make());
        FloatProperty<Component> spawnedRotationProp = spawned.events()
                .event("rotation", FloatProperty.<Component>make());

        spawnedPositionProp.ref().set(tmp);
        spawnedRotationProp.setSilently(rotation + entityRotationProp.get());

        spawnedPositionProp.trigger(this);
        spawnedRotationProp.trigger(this);

        return true;
    }

    protected boolean checkSpawn() {
        return true;
    }
}
