package com.github.alexeybond.gdx_commons.game.systems.input;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.gdx_commons.game.event.Events;

/**
 *
 */
public class InputSystemGestureListener implements GestureDetector.GestureListener {
    private final Events<InputSystem> events;
    private final InputSystem system;

    public InputSystemGestureListener(Events<InputSystem> events, InputSystem system) {
        this.events = events;
        this.system = system;
    }

    // TODO:: Add gesture input events

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
