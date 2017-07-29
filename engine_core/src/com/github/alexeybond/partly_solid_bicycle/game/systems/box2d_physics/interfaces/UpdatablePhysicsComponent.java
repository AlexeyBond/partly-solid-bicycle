package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.interfaces;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.util.updatable.Updatable;

/**
 * Physical component that needs to be updated in every frame.
 */
public interface UpdatablePhysicsComponent extends Component, Updatable {
}
