package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.interfaces;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.CollisionData;

/**
 * A physics component that may collide with others.
 *
 * When such component creates a fixture/body it should set itself as user data to receive collision callbacks.
 */
public interface CollidablePhysicsComponent extends Component {
    /**
     * @return entity this component is part of
     */
    Entity entity();

    void onBeginCollision(CollisionData collision);

    void onEndCollision(CollisionData collision);
}
