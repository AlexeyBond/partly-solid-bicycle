package com.github.alexeybond.gdx_commons.game.common_components.decl;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.common_components.DestroyOnEventComponent;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;

/**
 *
 */
public class DestroyOnEventDecl implements ComponentDeclaration {
    public String event;
    public String preDestroyEvent = "preDestroy";

    public boolean initEvent = false;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        return new DestroyOnEventComponent(event, initEvent, preDestroyEvent);
    }
}
