package com.github.alexeybond.partly_solid_bicycle.game.systems.input;

import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.GameSystem;
import com.github.alexeybond.partly_solid_bicycle.input.InputEvents;

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
