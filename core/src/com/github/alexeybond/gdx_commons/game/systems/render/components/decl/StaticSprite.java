package com.github.alexeybond.gdx_commons.game.systems.render.components.decl;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.render.components.StaticSpriteComponent;

/**
 *
 */
public class StaticSprite implements ComponentDeclaration {
    public String pass = "game-objects";

    public String sprite;

    @Override
    public Component create(GameDeclaration gameDeclaration) {
        return new StaticSpriteComponent(pass, sprite);
    }
}
