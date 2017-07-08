package com.github.alexeybond.gdx_gm2.test_game.game.components.decl;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_gm2.test_game.game.components.SpaceshipFuelTank;

/**
 *
 */
public class FuelTankDecl implements ComponentDeclaration {
    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        return new SpaceshipFuelTank();
    }
}
