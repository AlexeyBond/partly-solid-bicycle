package com.github.alexeybond.gdx_commons.game.systems.box2d_physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.GameSystem;
import com.github.alexeybond.gdx_commons.util.event.Events;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.IntProperty;
import com.github.alexeybond.gdx_commons.util.updatable.UnorederedUpdateGroup;
import com.github.alexeybond.gdx_commons.util.updatable.UpdateGroup;

import java.util.ArrayList;

/**
 *
 */
public class PhysicsSystem implements GameSystem, ContactListener {
    static {
        Box2D.init();
    }

    public static int RESERVE_COMPONENTS_CAPACITY = 32;

    private final Events<PhysicsSystem> events = new Events<PhysicsSystem>();
    private final FloatProperty<PhysicsSystem> autoTimeScale
            = events.event("autoTimeScale", FloatProperty.<PhysicsSystem>make(1f));
    private final FloatProperty<PhysicsSystem> timeAccumulator
            = events.event("timeAccumulator", FloatProperty.<PhysicsSystem>make(0));
    private final FloatProperty<PhysicsSystem> simulationStep
            = events.event("simulationStep", FloatProperty.<PhysicsSystem>make(0.01f));
    private final IntProperty<PhysicsSystem> positionIterations
            = events.event("positionIterations", IntProperty.<PhysicsSystem>make(2));
    private final IntProperty<PhysicsSystem> velocityIterations
            = events.event("velocityIterations", IntProperty.<PhysicsSystem>make(6));

    private World world;
    private UpdateGroup<PhysicsComponent> components
            = new UnorederedUpdateGroup<PhysicsComponent>(RESERVE_COMPONENTS_CAPACITY);

    private boolean isUpdating = false;
    private ArrayList<PhysicsComponent> disposeQueue = new ArrayList<PhysicsComponent>(16);

    private void disposeEnqueued() {
        for (int i = 0; i < disposeQueue.size(); i++) {
            disposeQueue.get(i).dispose();
        }

        disposeQueue.clear();
    }

    public Events<PhysicsSystem> events() {
        return events;
    }

    public World world() {
        return world;
    }

    @Override
    public void onConnect(Game game) {
        world = new World(Vector2.Zero, true);
        world.setContactListener(this);
        timeAccumulator.set(this, 0);
    }

    @Override
    public void onDisconnect(Game game) {
        world.dispose();
    }

    @Override
    public void update(float deltaTime) {
        disposeEnqueued();

        deltaTime = Math.min(deltaTime, 0.25f);

        float acc = timeAccumulator.get() + autoTimeScale.get() * deltaTime;
        float step = simulationStep.get();
        int pi = positionIterations.get(), vi = velocityIterations.get();

        isUpdating = true;

        while (acc >= step) {
            world.step(step, vi, pi);

            acc -= step;
        }

        isUpdating = false;

        timeAccumulator.set(this, acc);

        components.updateItems();
    }

    private boolean getContactingComponents(CollidablePhysicsComponent[] components, Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        components[0] = (CollidablePhysicsComponent) fixtureA.getUserData();
        components[1] = (CollidablePhysicsComponent) fixtureB.getUserData();

        if (null == components[0]) components[0] = (CollidablePhysicsComponent) fixtureA.getBody().getUserData();
        if (null == components[1]) components[1] = (CollidablePhysicsComponent) fixtureB.getBody().getUserData();

        return components[0] != null && components[1] != null;
    }

    private CollidablePhysicsComponent[] tmpComponents = new CollidablePhysicsComponent[2];
    private final CollisionData collisionData = new CollisionData();

    @Override
    public void beginContact(Contact contact) {
        if (getContactingComponents(tmpComponents, contact)) {
            collisionData.contact = contact;

            collisionData.isContactB = false;
            collisionData.that = tmpComponents[1];
            tmpComponents[0].onBeginCollision(collisionData);

            collisionData.isContactB = true;
            collisionData.that = tmpComponents[0];
            tmpComponents[1].onBeginCollision(collisionData);
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (getContactingComponents(tmpComponents, contact)) {
            collisionData.contact = contact;

            collisionData.isContactB = false;
            collisionData.that = tmpComponents[1];
            tmpComponents[0].onEndCollision(collisionData);

            collisionData.isContactB = true;
            collisionData.that = tmpComponents[0];
            tmpComponents[1].onEndCollision(collisionData);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public void registerComponent(PhysicsComponent component) {
        components.addItem(component);
    }

    /**
     * Calls {@link PhysicsComponent#dispose()} immediately or later if world update is in progress.
     */
    public void disposeComponent(PhysicsComponent component) {
        if (isUpdating) {
            disposeQueue.add(component);
        } else {
            component.dispose();
        }
    }
}
