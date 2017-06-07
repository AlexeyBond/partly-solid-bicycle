package com.github.alexeybond.gdx_gm2.test_game.game.components.decl;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_gm2.test_game.game.components.SpaceshipEngines;

/**
 *
 */
public class SpaceshipEnginesDecl implements ComponentDeclaration {
    @Override
    public Component create(GameDeclaration gameDeclaration) {
        return new SpaceshipEngines();
    }
}
