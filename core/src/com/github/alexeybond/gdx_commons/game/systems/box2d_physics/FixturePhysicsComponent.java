package com.github.alexeybond.gdx_commons.game.systems.box2d_physics;

import com.badlogic.gdx.physics.box2d.Fixture;

/**
 *
 */
public interface FixturePhysicsComponent extends CollidablePhysicsComponent {
    Fixture fixture();
}
