package com.github.alexeybond.partly_solid_bicycle.application.impl.layers;

import com.github.alexeybond.partly_solid_bicycle.application.Screen;
import com.github.alexeybond.partly_solid_bicycle.game.Game;

/**
 *
 */
public abstract class GameLayer extends LayerAdapter {
    private Game game;
    private Screen screen;

    @Override
    public void update(float dt) {
        game.update(dt);
    }

    @Override
    public void onConnect(Screen screen) {
        this.screen = screen;
        this.game = setupGame(this.game = new Game());
    }

    @Override
    public void onDisconnect(Screen screen) {
        this.game.dispose();
        this.game = null;
    }

    public Screen screen() {
        return screen;
    }

    public Game game() {
        return game;
    }

    protected Game setupGame(Game game) {
        return game;
    }
}
