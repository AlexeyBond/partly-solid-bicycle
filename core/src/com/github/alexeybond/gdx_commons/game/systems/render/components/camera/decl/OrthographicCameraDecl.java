package com.github.alexeybond.gdx_commons.game.systems.render.components.camera.decl;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.render.components.camera.OrthographicCameraComponent;

/**
 *
 */
public class OrthographicCameraDecl implements ComponentDeclaration {
    public String pass = "setup-main-camera";

    public String globalAlias = "mainCamera";

    @Override
    public Component create(GameDeclaration gameDeclaration) {
        return new OrthographicCameraComponent(pass, globalAlias);
    }
}
