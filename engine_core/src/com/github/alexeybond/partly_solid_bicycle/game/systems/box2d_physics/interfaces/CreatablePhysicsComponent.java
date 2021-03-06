package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.interfaces;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.util.interfaces.Creatable;

/**
 * Physical component that needs to create an associated physical object.
 *
 * Physical objects may not be created inside of physics update loop so physical system provides a
 * way to create component after loop exit.
 */
public interface CreatablePhysicsComponent extends Component, Creatable {
}
