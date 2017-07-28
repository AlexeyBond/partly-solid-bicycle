package com.github.alexeybond.partly_solid_bicycle.demos.test_game.game.components.decl;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.demos.test_game.game.components.FuelItem;

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
