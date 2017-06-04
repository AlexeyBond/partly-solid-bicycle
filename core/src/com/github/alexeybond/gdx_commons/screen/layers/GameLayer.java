package com.github.alexeybond.gdx_commons.screen.layers;

import com.badlogic.gdx.InputProcessor;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.systems.input.InputSystem;
import com.github.alexeybond.gdx_commons.game.systems.render.RenderSystem;
import com.github.alexeybond.gdx_commons.game.systems.timing.TimingSystem;
import com.github.alexeybond.gdx_commons.screen.ALayer;
import com.github.alexeybond.gdx_commons.screen.AScreen;

/**
 *
 */
public class GameLayer extends ALayer {
    private final Game game;

    public GameLayer(AScreen screen) {
        super(screen);

        game = new Game();

        setupGameSubsystems(game);
    }

    public Game game() {
        return game;
    }

    protected void setupGameSubsystems(Game game) {
        game.systems().add("timing", new TimingSystem());
        game.systems().add("render", new RenderSystem(screen().scene()));
        game.systems().add("input", new InputSystem(screen().viewport()));
    }

    @Override
    public InputProcessor getInputProcessor() {
        return game.systems().<InputSystem>get("input").inputProcessor();
    }

    @Override
    public void update(float dt) {
        game.update(dt);
    }
}
