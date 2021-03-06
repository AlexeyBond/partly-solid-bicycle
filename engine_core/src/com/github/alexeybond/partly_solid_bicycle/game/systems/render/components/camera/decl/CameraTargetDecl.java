package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.camera.decl;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.camera.CameraTargetComponent;

/**
 *
 */
public class CameraTargetDecl implements ComponentDeclaration {
    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        return new CameraTargetComponent();
    }
}
