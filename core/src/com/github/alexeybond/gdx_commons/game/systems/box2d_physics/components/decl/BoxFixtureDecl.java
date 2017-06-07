package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.decl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.decl.FixtureDeclBase;

/**
 *
 */
public class BoxFixtureDecl extends FixtureDeclBase {
    public float height = 20f;
    public float width = 20f;
    public float angle = 0f;
    public float centerX = 0;
    public float centerY = 0;

    @Override
    protected Shape initShape() {
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(.5f * width, .5f * height, new Vector2(centerX, centerY), angle);

        return shape;
    }
}
