package com.github.alexeybond.partly_solid_bicycle.game.common_components.decl;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.common_components.CollisionEventFilter;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;

/**
 *
 */
public class CollisionEventFilterDecl implements ComponentDeclaration {
    public String collisionEvent;

    public String filteredEvent;

    public String filterTag;

    public boolean invert = false;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        return new CollisionEventFilter(collisionEvent, filteredEvent, filterTag, invert);
    }
}
