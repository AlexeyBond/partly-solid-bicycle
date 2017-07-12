package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.interfaces;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Physical component that has an associated {@link Body body} object.
 */
public interface BodyPhysicsComponent extends UpdatablePhysicsComponent {
    Body body();
}
