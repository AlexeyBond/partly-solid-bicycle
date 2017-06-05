package com.github.alexeybond.gdx_commons.game.systems.box2d_physics;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.util.updatable.Updatable;

/**
 *
 */
public interface PhysicsComponent extends Component, Updatable {
    void dispose();
}
