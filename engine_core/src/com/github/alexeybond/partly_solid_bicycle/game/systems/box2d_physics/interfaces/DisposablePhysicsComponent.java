package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.interfaces;

import com.badlogic.gdx.utils.Disposable;
import com.github.alexeybond.partly_solid_bicycle.game.Component;

/**
 * A physical component that may be destroyed.
 *
 * Most of physical components have associated objects that should be destroyed outside of
 * physics update loop. So physics system provides methods to do so.
 */
public interface DisposablePhysicsComponent extends Disposable, Component {
}
