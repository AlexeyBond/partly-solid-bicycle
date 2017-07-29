package com.github.alexeybond.partly_solid_bicycle.game.systems.render;

import com.github.alexeybond.partly_solid_bicycle.drawing.Scene;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.GameSystem;

/**
 *
 */
public class RenderSystem implements GameSystem {
    private final Scene scene;

    public RenderSystem(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void onConnect(Game game) {

    }

    @Override
    public void onDisconnect(Game game) {

    }

    @Override
    public void update(float deltaTime) {

    }

    public void addToPass(String passName, com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.RenderComponent component) {
        scene.getPass(passName).addDrawable(component);
    }

    public void removeFromPass(String passName, com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.RenderComponent component) {
        scene.getPass(passName).removeDrawable(component);
    }
}
