package com.github.alexeybond.gdx_commons.game.common_components.decl;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.common_components.SendEventToGame;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;

/**
 *
 */
public class SendEventToGameDecl implements ComponentDeclaration {
    public String gameEvent;

    public String ownerEvent;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        return new SendEventToGame(gameEvent, ownerEvent);
    }
}
