package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.camera.decl;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.camera.OrthographicCameraComponent;

/**
 *
 */
public class OrthographicCameraDecl implements ComponentDeclaration {
    public String pass = "setup-main-camera";

    public String globalAlias = "mainCamera";

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        return new OrthographicCameraComponent(pass, globalAlias);
    }
}
