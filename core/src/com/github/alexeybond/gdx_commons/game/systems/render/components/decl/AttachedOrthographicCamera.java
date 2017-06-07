package com.github.alexeybond.gdx_commons.game.systems.render.components.decl;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.render.components.OrthographicCameraComponent;

/**
 *
 */
public class AttachedOrthographicCamera implements ComponentDeclaration {
    public String pass = "game-camera";

    @Override
    public Component create() {
        return new OrthographicCameraComponent(pass);
    }
}
