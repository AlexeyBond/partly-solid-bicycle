package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;

/**
 * Component that creates a body without any fixtures.
 */
public class EmptyBodyComponent extends BaseBodyComponent {

    private final BodyDef bodyDef;

    public EmptyBodyComponent(BodyDef bodyDef) {
        this.bodyDef = bodyDef;
    }

    public EmptyBodyComponent(BodyDef.BodyType bodyType) {
        this(new BodyDef());
        bodyDef.type = bodyType;
    }

    public EmptyBodyComponent(String typeName) {
        this(BodyDef.BodyType.valueOf(typeName));
    }

    @Override
    protected Body createBody() {
        return system().world().createBody(bodyDef);
    }
}
