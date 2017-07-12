package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.interfaces;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.util.updatable.Updatable;

/**
 * Physical component that needs to be updated in every frame.
 */
public interface UpdatablePhysicsComponent extends Component, Updatable {
}
