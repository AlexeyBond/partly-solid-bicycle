package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.CollisionData;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.interfaces.*;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.ObjectProperty;

/**
 *
 */
public abstract class BaseFixtureComponent
        implements FixturePhysicsComponent, DisposablePhysicsComponent, CreatablePhysicsComponent {
    private BaseBodyComponent bodyComponent;
    private Entity entity;
    private Fixture fixture;
    private final String collisionBeginEventName, collisionEndEventName;

    private ObjectProperty<CollisionData> collisionBeginEvent;
    private ObjectProperty<CollisionData> collisionEndEvent;

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
    public Fixture fixture() {
        return fixture;
    }

    @Override
    public void onConnect(Entity entity) {
        this.entity = entity;
        bodyComponent = entity.components().get("body");
        collisionBeginEvent = entity.events().event(
                        collisionBeginEventName,
                        ObjectProperty.<CollisionData>make());
        collisionEndEvent = entity.events().event(
                        collisionEndEventName,
                        ObjectProperty.<CollisionData>make());
        entity.game().systems().<APhysicsSystem>get("physics").createComponent(this);
    }

    @Override
    public void onDisconnect(Entity entity) {
        entity.game().systems().<APhysicsSystem>get("physics").disposeComponent(this);
    }

    @Override
    public void create() {
        fixture = createFixture();
        fixture.setUserData(this);
    }

    @Override
    public void dispose() {
        // If parent is (going to be) destroyed then do nothing
        if (parent().isAlive()) {
            bodyComponent.body().destroyFixture(fixture);
            fixture = null;
        }
    }

    @Override
    public Entity entity() {
        return entity;
    }

    @Override
    public void onBeginCollision(CollisionData collision) {
        collisionBeginEvent.setSilently(collision);
        collisionBeginEvent.trigger();
    }

    @Override
    public void onEndCollision(CollisionData collision) {
        collisionEndEvent.setSilently(collision);
        collisionEndEvent.trigger();
    }

    public BaseBodyComponent parent() {
        return bodyComponent;
    }

    protected abstract Fixture createFixture();
}
