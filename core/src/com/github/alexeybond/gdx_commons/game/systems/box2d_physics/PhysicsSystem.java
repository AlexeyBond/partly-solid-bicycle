package com.github.alexeybond.gdx_commons.game.systems.box2d_physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Queue;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.GameSystem;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.interfaces.*;
import com.github.alexeybond.gdx_commons.util.event.Events;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.IntProperty;
import com.github.alexeybond.gdx_commons.util.number_allocator.IncrementalSequenceNumberAllocator;
import com.github.alexeybond.gdx_commons.util.number_allocator.NamedNumberAllocator;
import com.github.alexeybond.gdx_commons.util.number_allocator.PowerOfTwoSequenceNumberAllocator;
import com.github.alexeybond.gdx_commons.util.updatable.UnorederedUpdateGroup;
import com.github.alexeybond.gdx_commons.util.updatable.UpdateGroup;

/**
 *
 */
public class PhysicsSystem implements GameSystem, ContactListener, APhysicsSystem {
    static {
        Box2D.init();
    }

    public static int RESERVE_COMPONENTS_CAPACITY = 32;

    private final Events<APhysicsSystem> events = new Events<APhysicsSystem>();
    private final FloatProperty<APhysicsSystem> autoTimeScale
            = events.event("autoTimeScale", FloatProperty.<APhysicsSystem>make(1f));
    private final FloatProperty<APhysicsSystem> timeAccumulator
            = events.event("timeAccumulator", FloatProperty.<APhysicsSystem>make(0));
    private final FloatProperty<APhysicsSystem> simulationStep
            = events.event("simulationStep", FloatProperty.<APhysicsSystem>make(0.01f));
    private final IntProperty<APhysicsSystem> positionIterations
            = events.event("positionIterations", IntProperty.<APhysicsSystem>make(2));
    private final IntProperty<APhysicsSystem> velocityIterations
            = events.event("velocityIterations", IntProperty.<APhysicsSystem>make(6));

    private World world;
    private UpdateGroup<UpdatablePhysicsComponent> components
            = new UnorederedUpdateGroup<UpdatablePhysicsComponent>(RESERVE_COMPONENTS_CAPACITY);

    private boolean isUpdating = false;
    private Queue<DisposablePhysicsComponent> disposeQueue = new Queue<DisposablePhysicsComponent>(16);
    private Queue<CreatablePhysicsComponent> createQueue = new Queue<CreatablePhysicsComponent>(16);

    private final NamedNumberAllocator categoryAllocator = new PowerOfTwoSequenceNumberAllocator(Short.MAX_VALUE);
    {categoryAllocator.resolve("default");}
    private final NamedNumberAllocator selfCollidableGroupAllocator = new IncrementalSequenceNumberAllocator(1, 1, Short.MAX_VALUE);
    private final NamedNumberAllocator nonSelfCollidableGroupAllocator = new IncrementalSequenceNumberAllocator(-1, -1, Short.MIN_VALUE);

    public short collisionGroup(String name) {
        if (name.charAt(0) == '+') {
            return (short) selfCollidableGroupAllocator.resolve(name);
        } else {
            return (short) nonSelfCollidableGroupAllocator.resolve(name);
        }
    }

    public short categoryFlag(String categoryName) {
        return (short) categoryAllocator.resolve(categoryName);
    }

    private void executeEnqueued() {
        while (0 != createQueue.size)
            createQueue.removeFirst().create();

        while (0 != disposeQueue.size)
            disposeQueue.removeFirst().dispose();
    }

    public Events<APhysicsSystem> events() {
        return events;
    }

    @Override
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

        try {
            isUpdating = true;

            while (acc >= step) {
                world.step(step, vi, pi);

                acc -= step;
            }
        } finally {
            isUpdating = false;
        }

        timeAccumulator.set(this, acc);

        executeEnqueued();

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

    @Override
    public void registerComponent(UpdatablePhysicsComponent component) {
        components.addItem(component);
    }

    /**
     * Calls {@link DisposablePhysicsComponent#dispose()} immediately or later if world update is in progress.
     */
    public void disposeComponent(DisposablePhysicsComponent component) {
        if (isUpdating) {
            disposeQueue.addLast(component);
        } else {
            component.dispose();
        }
    }

    public void createComponent(CreatablePhysicsComponent component) {
        if (isUpdating) {
            createQueue.addLast(component);
        } else {
            component.create();
        }
    }
}
