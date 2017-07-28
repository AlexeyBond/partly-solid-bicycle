package com.github.alexeybond.partly_solid_bicycle.demos.test_game.game.components.decl;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.demos.test_game.game.components.GravityTrigger;

/**
 *
 */
public class GravityTriggerDecl implements ComponentDeclaration {
    public String hitBeginEvent;
    public String hitEndEvent;

    public String attractorName;

    public String enableEvent = "gravityEnabled";

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        return new GravityTrigger(hitBeginEvent, hitEndEvent, attractorName, enableEvent);
    }
}
