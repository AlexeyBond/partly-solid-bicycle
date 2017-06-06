package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.decl;

import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.decl.FixtureDeclBase;

/**
 *
 */
public class PolygonFixtureDecl extends FixtureDeclBase {
    public float[] vertices;

    @Override
    protected Shape initShape() {
        PolygonShape shape = new PolygonShape();
        shape.set(vertices);
        return shape;
    }
}
