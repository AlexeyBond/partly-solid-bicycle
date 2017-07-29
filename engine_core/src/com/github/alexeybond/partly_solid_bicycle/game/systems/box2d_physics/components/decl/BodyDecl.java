package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.components.decl;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.components.EmptyBodyComponent;

/**
 *
 */
public class BodyDecl implements ComponentDeclaration {
    private transient BodyDef bodyDef = null;

    public String bodyType = "DynamicBody";
    public boolean isBullet = true;
    public boolean noRotation = false;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        initDef();
        return new EmptyBodyComponent(bodyDef);
    }

    private void initDef() {
        if (null != bodyDef) return;

        bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.valueOf(bodyType);
        bodyDef.bullet = isBullet;
        bodyDef.fixedRotation = noRotation;
    }
}
