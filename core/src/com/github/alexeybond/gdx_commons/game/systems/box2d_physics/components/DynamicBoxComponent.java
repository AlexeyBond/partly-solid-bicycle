package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 *
 */
// TODO:: Remove
public class DynamicBoxComponent extends BaseBodyComponent {
    @Override
    protected Body createBody() {
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;

        Body body = world().createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.density = 0.5f;

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(256, 32, new Vector2(0,0), 0);

        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);

        return body;
    }
}
