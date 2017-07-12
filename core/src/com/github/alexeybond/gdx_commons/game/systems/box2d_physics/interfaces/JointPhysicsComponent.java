package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.interfaces;

import com.badlogic.gdx.physics.box2d.Joint;

/**
 * Physical component that has an associated {@link Joint joint} object.
 */
public interface JointPhysicsComponent {
    Joint joint();
}
