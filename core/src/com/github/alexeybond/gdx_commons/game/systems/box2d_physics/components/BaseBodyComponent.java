package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.CollidablePhysicsComponent;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.PhysicsSystem;

/**
 *
 */
public abstract class BaseBodyComponent
        implements CollidablePhysicsComponent, EventListener<Component, Event<Component>> {
    private boolean alive = false;
    private Entity entity;

    protected Vec2Property<Component> positionProp;
    protected FloatProperty<Component> rotationProp;

    private int positionSubIdx, rotationSubIdx;

    private ObjectProperty<Entity, Component> collisionStartEntityProp;
    private ObjectProperty<Entity, Component> collisionEndEntityProp;

    private Body body;

    private PhysicsSystem system;

    @Override
    public void update() {
        Transform transform = body.getTransform();
        positionProp.set(this, transform.getPosition());
        rotationProp.set(this, MathUtils.radiansToDegrees * transform.getRotation());
            // TODO:: Use body.getAngle()?, seems to be more efficient
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public Entity entity() {
        return entity;
    }

    @Override
    public void onBeginCollision(CollidablePhysicsComponent with, Contact contact, boolean isB) {
        collisionStartEntityProp.setSilently(with.entity());
        collisionStartEntityProp.trigger(this);
    }

    @Override
    public void onEndCollision(CollidablePhysicsComponent with, Contact contact, boolean isB) {
        collisionEndEntityProp.setSilently(with.entity());
        collisionEndEntityProp.trigger(this);
    }

    @Override
    public void onConnect(Entity entity) {
        system = entity.game().systems().get("physics");

        body = createBody();

        this.entity = entity;
        this.alive = true;

        positionProp = entity.events().event("position", Vec2Property.<Component>make());
        rotationProp = entity.events().event("rotation", FloatProperty.<Component>make());

        collisionStartEntityProp = entity.events().event("collisionStart", ObjectProperty.<Entity, Component>make());
        collisionEndEntityProp = entity.events().event("collisionEnd", ObjectProperty.<Entity, Component>make());

        positionSubIdx = positionProp.subscribe(this);
        rotationSubIdx = rotationProp.subscribe(this);

        system.registerComponent(this);
    }

    @Override
    public void onDisconnect(Entity entity) {
        this.alive = false;

        positionSubIdx = positionProp.unsubscribe(positionSubIdx);
        rotationSubIdx = rotationProp.unsubscribe(rotationSubIdx);
    }

    /**
     * Called when entity transform is changed by this or another component.
     */
    @Override
    public boolean onTriggered(Component o, Event<Component> event) {
        if (o == this) return false; // Avoid loop

        body.setTransform(positionProp.ref(), MathUtils.degreesToRadians * rotationProp.get());

        return true;
    }

    protected abstract Body createBody();

    public PhysicsSystem system() {
        return system;
    }

    public Body body() {
        return body;
    }

    protected World world() {
        return system.world();
    }
}
