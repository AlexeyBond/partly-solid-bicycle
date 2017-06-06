package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.decl;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.FixtureDefFixtureComponent;

/**
 *
 */
abstract class FixtureDeclBase implements ComponentDeclaration {
    private transient FixtureDef fixtureDef = null;

    public String collisionBeginEvent = "collisionBegin";
    public String collisionEndEvent = "collisionEnd";

    public float density = 0.01f;
    public float restitution = 0;
    public float friction = 0.2f;
    public boolean sensor = false;

    @Override
    public Component create() {
        initFixtureDef();
        return new FixtureDefFixtureComponent(
                collisionBeginEvent, collisionEndEvent, fixtureDef);
    }

    private void initFixtureDef() {
        if (null != fixtureDef) return;

        fixtureDef = new FixtureDef();
        fixtureDef.shape = initShape();
        fixtureDef.density = density;
        fixtureDef.isSensor = sensor;
        fixtureDef.restitution = restitution;
        fixtureDef.friction = friction;
    }

    protected abstract Shape initShape();

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        // (PROBABLY) This should be enough
        if (null != fixtureDef && null != fixtureDef.shape) {
            fixtureDef.shape.dispose();
            fixtureDef.shape = null;
        }
    }
}
