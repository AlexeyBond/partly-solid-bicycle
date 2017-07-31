package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Queue;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.GameSystem;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.interfaces.*;
import com.github.alexeybond.partly_solid_bicycle.util.event.Events;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.FloatProperty;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.IntProperty;
import com.github.alexeybond.partly_solid_bicycle.util.interfaces.Creatable;
import com.github.alexeybond.partly_solid_bicycle.util.number_allocator.IncrementalSequenceNumberAllocator;
import com.github.alexeybond.partly_solid_bicycle.util.number_allocator.NamedNumberAllocator;
import com.github.alexeybond.partly_solid_bicycle.util.number_allocator.PowerOfTwoSequenceNumberAllocator;
import com.github.alexeybond.partly_solid_bicycle.util.updatable.UnorderedUpdateGroup;
import com.github.alexeybond.partly_solid_bicycle.util.updatable.UpdateGroup;

/**
 *
 */
public class PhysicsSystem implements GameSystem, ContactListener, APhysicsSystem {
    static {
        Box2D.init();
    }

    public static int RESERVE_COMPONENTS_CAPACITY = 32;

    private final Events events = new Events();
    private final FloatProperty autoTimeScale
            = events.event("autoTimeScale", FloatProperty.make(1f));
    private final FloatProperty timeAccumulator
            = events.event("timeAccumulator", FloatProperty.make(0));
    private final FloatProperty simulationStep
            = events.event("simulationStep", FloatProperty.make(0.01f));
    private final IntProperty positionIterations
            = events.event("positionIterations", IntProperty.make(2));
    private final IntProperty velocityIterations
            = events.event("velocityIterations", IntProperty.make(6));

    private World world;
    private UpdateGroup<UpdatablePhysicsComponent> components
            = new UnorderedUpdateGroup<UpdatablePhysicsComponent>(RESERVE_COMPONENTS_CAPACITY);

    private boolean isUpdating = false;
    private Queue<Disposable> disposeQueue = new Queue<Disposable>(16);
    private Queue<Creatable> createQueue = new Queue<Creatable>(16);

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

    public Events events() {
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
        timeAccumulator.set(0);
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

        timeAccumulator.set(acc);

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

    @Override
    public void enqueueDisposable(Disposable component) {
        if (isUpdating) {
            disposeQueue.addLast(component);
        } else {
            component.dispose();
        }
    }

    @Override
    public void disposeComponent(DisposablePhysicsComponent component) {
        enqueueDisposable(component);
    }

    @Override
    public void createComponent(CreatablePhysicsComponent component) {
        enqueueCreatable(component);
    }

    @Override
    public void enqueueCreatable(Creatable creatable) {
        if (isUpdating) {
            createQueue.addLast(creatable);
        } else {
            creatable.create();
        }
    }
}
