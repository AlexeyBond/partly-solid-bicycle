package com.github.alexeybond.gdx_commons.game.systems.tagging.components.decl;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.tagging.components.SingleTagComponent;

/**
 *
 */
public class SingleTag implements ComponentDeclaration {
    public String tag;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        return new SingleTagComponent(tag);
    }
}
