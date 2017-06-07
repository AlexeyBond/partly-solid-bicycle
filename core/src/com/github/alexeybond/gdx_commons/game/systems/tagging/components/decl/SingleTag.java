package com.github.alexeybond.gdx_commons.game.systems.tagging.components.decl;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.tagging.components.SingleTagComponent;

/**
 *
 */
public class SingleTag implements ComponentDeclaration {
    public String tag;

    @Override
    public Component create() {
        return new SingleTagComponent(tag);
    }
}
