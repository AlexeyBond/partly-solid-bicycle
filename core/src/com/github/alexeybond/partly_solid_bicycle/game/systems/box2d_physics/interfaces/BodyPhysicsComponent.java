package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.interfaces;

import com.badlogic.gdx.physics.box2d.Body;
import com.github.alexeybond.partly_solid_bicycle.game.Component;

/**
 * Physical component that has an associated {@link Body body} object.
 */
public interface BodyPhysicsComponent extends Component {
    Body body();
}
