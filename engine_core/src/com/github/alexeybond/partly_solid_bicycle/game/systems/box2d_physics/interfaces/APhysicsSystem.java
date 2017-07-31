package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.interfaces;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.github.alexeybond.partly_solid_bicycle.game.GameSystem;
import com.github.alexeybond.partly_solid_bicycle.util.event.EventsOwner;
import com.github.alexeybond.partly_solid_bicycle.util.interfaces.Creatable;

/**
 * Interface of physics system.
 */
public interface APhysicsSystem extends EventsOwner, GameSystem {
    /** The physical world. */
    World world();

    /**
     * Get index of a named collision group.
     *
     * If group name starts with a {@code '+'} character then will be returned self-colliding group index (positive),
     * non-self-colliding group index (negative) will be returned else.
     */
    short collisionGroup(String name);

    /**
     * Get a flag for named collision category.
     *
     * @see com.badlogic.gdx.physics.box2d.Filter#categoryBits
     * @see com.badlogic.gdx.physics.box2d.Filter#maskBits
     */
    short categoryFlag(String categoryName);

    /**
     * Dispose a component or enqueue it for disposal if physics update is in progress.
     */
    void disposeComponent(DisposablePhysicsComponent component);

    /**
     * Works as {@link #disposeComponent(DisposablePhysicsComponent)} but supports disposable objects of any type.
     */
    void enqueueDisposable(Disposable disposable);

    /**
     * Create a component or enqueue it for creation if physics update is in progress.
     */
    void createComponent(CreatablePhysicsComponent component);

    /**
     * Works as {@link #createComponent(CreatablePhysicsComponent)} but supports creatable objects of any type.
     */
    void enqueueCreatable(Creatable creatable);

    /**
     * Register a component that should be updated on every frame.
     */
    void registerComponent(UpdatablePhysicsComponent component);
}
