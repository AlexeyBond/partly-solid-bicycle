package com.github.alexeybond.partly_solid_bicycle.game.common_components.decl;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.common_components.SendEventToGame;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;

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
