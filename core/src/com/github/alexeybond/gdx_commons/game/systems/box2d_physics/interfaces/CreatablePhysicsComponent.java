package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.interfaces;

import com.github.alexeybond.gdx_commons.game.Component;

/**
 * Physical component that needs to create an associated physical object.
 *
 * Physical objects may not be created inside of physics update loop so physical system provides a
 * way to create component after loop exit.
 */
public interface CreatablePhysicsComponent extends Component {
    void create();
}
