package com.github.alexeybond.gdx_commons.game.common_components.decl;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.common_components.CollisionEventFilter;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;

/**
 *
 */
public class CollisionEventFilterDecl implements ComponentDeclaration {
    public String collisionEvent;

    public String filteredEvent;

    public String filterTag;

    public boolean invert = false;

    @Override
    public Component create(GameDeclaration gameDeclaration) {
        return new CollisionEventFilter(collisionEvent, filteredEvent, filterTag, invert);
    }
}
