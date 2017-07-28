package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.components.decl;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 *
 */
public class CircleFixtureDecl extends FixtureDeclBase {
    public float radius = 10;

    @Override
    protected Shape initShape() {
        CircleShape shape = new CircleShape();

        shape.setRadius(radius);
        shape.setPosition(getCenter());

        return shape;
    }
}
