package com.github.alexeybond.gdx_commons.game.systems.render.components.camera.decl;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.render.components.camera.CameraController;

/**
 *
 */
public class CameraControllerDecl implements ComponentDeclaration {
    @Override
    public Component create(GameDeclaration gameDeclaration) {
        return new CameraController();
    }
}
