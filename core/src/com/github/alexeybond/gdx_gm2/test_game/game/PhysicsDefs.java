package com.github.alexeybond.gdx_gm2.test_game.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 *
 */
public class PhysicsDefs {
    public FixtureDef spaceshipBodyFixture = new FixtureDef();

    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(128, 96);

        spaceshipBodyFixture.shape = shape;
        spaceshipBodyFixture.density = 0.01f;
    }

    public FixtureDef spaceshipTriggerFixture = new FixtureDef();

    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(128, 64, new Vector2(0, 128), 0);

        spaceshipTriggerFixture.shape = shape;
        spaceshipTriggerFixture.density = 0;
        spaceshipTriggerFixture.isSensor = true;
    }

    public FixtureDef spaceshipAttractorTrigger = new FixtureDef();

    {
        PolygonShape shape = new PolygonShape();
        shape.set(new float[] {
                -64, 0,
                -256, 348,
                256, 348,
                64, 0
        });

        spaceshipAttractorTrigger.shape = shape;
        spaceshipAttractorTrigger.density = 0;
        spaceshipAttractorTrigger.isSensor = true;
    }
}
