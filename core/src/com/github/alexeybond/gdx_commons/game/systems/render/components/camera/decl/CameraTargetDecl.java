package com.github.alexeybond.gdx_commons.game.systems.render.components.camera.decl;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.render.components.camera.CameraTargetComponent;

/**
 *
 */
public class CameraTargetDecl implements ComponentDeclaration {
    @Override
    public Component create(GameDeclaration gameDeclaration) {
        return new CameraTargetComponent();
    }
}
