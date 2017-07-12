package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.interfaces;

import com.badlogic.gdx.physics.box2d.World;
import com.github.alexeybond.gdx_commons.game.GameSystem;
import com.github.alexeybond.gdx_commons.util.event.EventsOwner;

/**
 * Interface of physics system.
 */
public interface APhysicsSystem extends EventsOwner<APhysicsSystem>, GameSystem {
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
     * Create a component or enqueue it for creation if physics update is in progress.
     */
    void createComponent(CreatablePhysicsComponent component);

    /**
     * Register a component that should be updated on every frame.
     */
    void registerComponent(UpdatablePhysicsComponent component);
}
