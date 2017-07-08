package com.github.alexeybond.gdx_gm2.test_game.game.components.decl;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_gm2.test_game.game.components.FuelItem;

/**
 *
 */
public class FuelItemDecl implements ComponentDeclaration {
    public float defaultAmount = 10;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        return new FuelItem(defaultAmount);
    }
}
