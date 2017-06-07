package com.github.alexeybond.gdx_commons.game.systems.tagging.components.decl;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.tagging.components.TagsComponent;

/**
 *
 */
public class Tags implements ComponentDeclaration {
    @Override
    public Component create(GameDeclaration gameDeclaration) {
        return new TagsComponent();
    }
}
