package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.CollisionData;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.interfaces.*;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;

/**
 *
 */
public abstract class BaseBodyComponent
        implements CollidablePhysicsComponent, EventListener<Component, Event<Component>>,
        BodyPhysicsComponent, DisposablePhysicsComponent, CreatablePhysicsComponent,
        UpdatablePhysicsComponent {
    private boolean alive = false;
    private Entity entity;

    protected Vec2Property<Component> positionProp;
    protected FloatProperty<Component> rotationProp;

    private int positionSubIdx, rotationSubIdx;

    private ObjectProperty<CollisionData, Component> collisionBeginProp;
    private ObjectProperty<CollisionData, Component> collisionEndProp;

    private Body body;

    private APhysicsSystem system;

    private int skipTransformChanges = 0;

    @Override
    public void update() {
        try {
            skipTransformChanges = 1; // Ignore one position update
            positionProp.set(this, body.getPosition());

            skipTransformChanges = 1; // Ignore one rotation update
            rotationProp.set(this, MathUtils.radiansToDegrees * body.getAngle());
        } finally {
            skipTransformChanges = 0;
        }
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
    public void onBeginCollision(CollisionData collision) {
        collisionBeginProp.setSilently(collision);
        collisionBeginProp.trigger(this);
    }

    @Override
    public void onEndCollision(CollisionData collision) {
        collisionEndProp.setSilently(collision);
        collisionEndProp.trigger(this);
    }

    @Override
    public void onConnect(Entity entity) {
        system = entity.game().systems().get("physics");

        this.entity = entity;
        this.alive = true;

        positionProp = entity.events().event("position", Vec2Property.<Component>make());
        rotationProp = entity.events().event("rotation", FloatProperty.<Component>make());

        collisionBeginProp = entity.events()
                .event("collisionBegin", ObjectProperty.<CollisionData, Component>make());
        collisionEndProp = entity.events()
                .event("collisionEnd", ObjectProperty.<CollisionData, Component>make());

        positionSubIdx = positionProp.subscribe(this);
        rotationSubIdx = rotationProp.subscribe(this);

        system.createComponent(this);
    }

    @Override
    public void onDisconnect(Entity entity) {
        this.alive = false;

        positionSubIdx = positionProp.unsubscribe(positionSubIdx);
        rotationSubIdx = rotationProp.unsubscribe(rotationSubIdx);

        system.disposeComponent(this);
    }

    @Override
    public void create() {
        body = createBody();
        body.setUserData(this);

        // Do not update static bodies.
        if (body.getType() != BodyDef.BodyType.StaticBody) {
            system.registerComponent(this);
        }
    }

    @Override
    public void dispose() {
        world().destroyBody(body);
        body = null;
    }

    /**
     * Called when entity transform is changed by this or another component.
     */
    @Override
    public boolean onTriggered(Component o, Event<Component> event) {
        int skip = skipTransformChanges;

        if (skip > 0) {
            skipTransformChanges = skip - 1;
            return false;
        }

        body.setTransform(positionProp.ref(), MathUtils.degreesToRadians * rotationProp.get());

        return true;
    }

    protected abstract Body createBody();

    public APhysicsSystem system() {
        return system;
    }

    @Override
    public Body body() {
        return body;
    }

    protected World world() {
        return system.world();
    }
}
