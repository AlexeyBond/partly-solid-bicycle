package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.decl;

import com.badlogic.gdx.graphics.Texture;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.GlobalCameraBackgroundLoop;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;

/**
 *
 */
public class BackgroundLoop implements ComponentDeclaration {
    public String pass = "game-background";
    public String camera = "mainCamera";
    public String texture;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        Texture tx = IoC.resolve("load texture loop", texture);
        return new GlobalCameraBackgroundLoop(pass, tx, camera);
    }
}
