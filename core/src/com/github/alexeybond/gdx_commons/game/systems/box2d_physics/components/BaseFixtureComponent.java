package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.CollidablePhysicsComponent;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.CollisionData;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;

/**
 *
 */
public abstract class BaseFixtureComponent implements CollidablePhysicsComponent {
    private BaseBodyComponent bodyComponent;
    private Fixture fixture;
    private boolean alive = false;
    private final String collisionBeginEventName, collisionEndEventName;

    private ObjectProperty<CollisionData, Component> collisionBeginEvent;
    private ObjectProperty<CollisionData, Component> collisionEndEvent;

    protected BaseFixtureComponent(
            String collisionBeginEventName, String collisionEndEventName) {
        this.collisionBeginEventName = collisionBeginEventName;
        this.collisionEndEventName = collisionEndEventName;
    }

    /**
     * Constructor with default event names.
     */
    protected BaseFixtureComponent() {
        this("collisionBegin", "collisionEnd");
    }

    @Override
    public void onConnect(Entity entity) {
        bodyComponent = entity.components().get("body");
        collisionBeginEvent = entity.events().event(
                        collisionBeginEventName,
                        ObjectProperty.<CollisionData, Component>make());
        collisionEndEvent = entity.events().event(
                        collisionEndEventName,
                        ObjectProperty.<CollisionData, Component>make());
        fixture = createFixture();
        fixture.setUserData(this);
        alive = true;
    }

    @Override
    public void onDisconnect(Entity entity) {
        if (isAlive()) {
            bodyComponent.body().destroyFixture(fixture);
            fixture = null;
        }
    }

    @Override
    public void update() {
        // No update required
    }

    @Override
    public boolean isAlive() {
        return bodyComponent.isAlive() && alive;
    }

    @Override
    public Entity entity() {
        return bodyComponent.entity();
    }

    @Override
    public void onBeginCollision(CollisionData collision) {
        collisionBeginEvent.setSilently(collision);
        collisionBeginEvent.trigger(this);
    }

    @Override
    public void onEndCollision(CollisionData collision) {
        collisionEndEvent.setSilently(collision);
        collisionEndEvent.trigger(this);
    }

    public BaseBodyComponent parent() {
        return bodyComponent;
    }

    protected abstract Fixture createFixture();
}
