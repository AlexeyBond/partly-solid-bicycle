package com.github.alexeybond.gdx_commons.input.impl;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.gdx_commons.input.InputEvents;
import com.github.alexeybond.gdx_commons.util.event.Events;

/**
 *
 */
class InputEventsGestureListener implements GestureDetector.GestureListener {
    private final Events events;
    private final InputEvents owner;

    public InputEventsGestureListener(Events events, InputEvents owner) {
        this.events = events;
        this.owner = owner;
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
