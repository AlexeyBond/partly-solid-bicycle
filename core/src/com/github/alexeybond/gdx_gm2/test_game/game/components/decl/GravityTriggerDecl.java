package com.github.alexeybond.gdx_gm2.test_game.game.components.decl;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_gm2.test_game.game.components.GravityTrigger;

/**
 *
 */
public class GravityTriggerDecl implements ComponentDeclaration {
    public String hitBeginEvent;
    public String hitEndEvent;

    public String attractorName;

    public String enableEvent = "gravityEnabled";

    @Override
    public Component create(GameDeclaration gameDeclaration) {
        return new GravityTrigger(hitBeginEvent, hitEndEvent, attractorName, enableEvent);
    }
}
