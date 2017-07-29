package com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.components.decl;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.components.SingleTagComponent;

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
