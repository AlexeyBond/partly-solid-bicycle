package com.github.alexeybond.gdx_commons.game.systems.box2d_physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.interfaces.CollidablePhysicsComponent;

/**
 * Class for objects storing properties of collision event.
 *
 * Object(s) of this class will be reused so the data may and will become invalid immediately after event
 * listener terminates.
 */
public class CollisionData {
    public CollidablePhysicsComponent that;

    public Contact contact;

    public boolean isContactB;
}
