package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.interfaces;

import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Physical component that has an associated {@link Fixture fixture} object.
 */
public interface FixturePhysicsComponent extends CollidablePhysicsComponent {
    Fixture fixture();
}
