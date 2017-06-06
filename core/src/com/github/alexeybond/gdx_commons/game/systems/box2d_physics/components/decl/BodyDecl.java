package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.decl;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.EmptyBodyComponent;

/**
 *
 */
public class BodyDecl implements ComponentDeclaration {
    private transient BodyDef bodyDef = null;

    public String bodyType = "DynamicBody";
    public boolean isBullet = true;

    @Override
    public Component create() {
        initDef();
        return new EmptyBodyComponent(bodyDef);
    }

    private void initDef() {
        if (null != bodyDef) return;

        bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.valueOf(bodyType);
        bodyDef.bullet = isBullet;
    }
}
