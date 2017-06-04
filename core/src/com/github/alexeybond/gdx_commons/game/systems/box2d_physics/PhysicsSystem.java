package com.github.alexeybond.gdx_commons.game.systems.box2d_physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.GameSystem;
import com.github.alexeybond.gdx_commons.game.event.Events;
import com.github.alexeybond.gdx_commons.game.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.game.event.props.IntProperty;
import com.github.alexeybond.gdx_commons.game.updatable.UnorederedUpdateGroup;
import com.github.alexeybond.gdx_commons.game.updatable.UpdateGroup;

/**
 *
 */
public class PhysicsSystem implements GameSystem, ContactListener {
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
        deltaTime = Math.min(deltaTime, 0.25f);

        float acc = timeAccumulator.get() + autoTimeScale.get() * deltaTime;
        float step = simulationStep.get();
        int pi = positionIterations.get(), vi = velocityIterations.get();

        while (acc >= step) {
            world.step(step, vi, pi);

            acc -= step;
        }

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

    @Override
    public void beginContact(Contact contact) {
        if (getContactingComponents(tmpComponents, contact)) {
            tmpComponents[0].onBeginCollision(tmpComponents[1], contact, false);
            tmpComponents[1].onBeginCollision(tmpComponents[0], contact, true);
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (getContactingComponents(tmpComponents, contact)) {
            tmpComponents[0].onEndCollision(tmpComponents[1], contact, false);
            tmpComponents[1].onEndCollision(tmpComponents[0], contact, true);
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
}
