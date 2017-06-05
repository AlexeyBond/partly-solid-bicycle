package com.github.alexeybond.gdx_commons.game.systems.input;

import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.GameSystem;
import com.github.alexeybond.gdx_commons.input.InputEvents;

/**
 *
 */
public class InputSystem implements GameSystem {
    private final InputEvents inputEvents;

    public InputSystem(InputEvents inputEvents) {
        this.inputEvents = inputEvents;
    }

    @Override
    public void onConnect(Game game) {
        inputEvents.enable();
    }

    @Override
    public void onDisconnect(Game game) {
        inputEvents.disable();
    }

    @Override
    public void update(float deltaTime) {

    }

    public InputEvents input() {
        return inputEvents;
    }
}
