package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.components.decl;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.components.decl.FixtureDeclBase;

/**
 *
 */
public class BoxFixtureDecl extends FixtureDeclBase {
    public float height = 20f;
    public float width = 20f;
    public float angle = 0f;

    public float[] size = new float[0];

    @Override
    protected Shape initShape() {
        PolygonShape shape = new PolygonShape();

        float w = width, h = height;

        if (size.length != 0) {
            w = size[0]; h = size[1];
        }

        shape.setAsBox(
                .5f * w, .5f * h,
                getCenter(),
                MathUtils.degreesToRadians * angle);

        return shape;
    }
}
