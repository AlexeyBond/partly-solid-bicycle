package com.github.alexeybond.gdx_commons.game.systems.box2d_physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.github.alexeybond.gdx_commons.game.Entity;

/**
 * A physics component that may collide with others.
 *
 * When such component creates a fixture/body it should set itself as user data to receive collision callbacks.
 */
public interface CollidablePhysicsComponent extends PhysicsComponent {
    /**
     * @return entity this component is part of
     */
    Entity entity();

    void onBeginCollision(CollidablePhysicsComponent with, Contact contact, boolean isB);

    void onEndCollision(CollidablePhysicsComponent with, Contact contact, boolean isB);
}
