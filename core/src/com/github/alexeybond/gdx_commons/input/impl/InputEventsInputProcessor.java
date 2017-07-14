package com.github.alexeybond.gdx_commons.input.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.github.alexeybond.gdx_commons.util.event.Events;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;
import com.github.alexeybond.gdx_commons.util.event.props.IntProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;

/**
 *
 */
class InputEventsInputProcessor implements InputProcessor {
    private final Events events;
    private final InputEventsImpl owner;

    private final Vec2Property mousePos;
    private final IntProperty scroll;
    private final BooleanProperty mouseDown;

    public InputEventsInputProcessor(Events events, InputEventsImpl owner) {
        this.events = events;
        this.owner = owner;

        mousePos = events.event("mousePos",
                Vec2Property.make(Gdx.input.getX(), Gdx.input.getY()));
        scroll = events.event("scroll",
                IntProperty.make(0));
        mouseDown = events.event("mouseDown",
                BooleanProperty.make(Gdx.input.isButtonPressed(Input.Buttons.LEFT)));
    }

    @Override
    public boolean keyDown(int keycode) {
        BooleanProperty event = events.nullableEvent(Input.Keys.toString(keycode));

        return null != event && event.set(true);
    }

    @Override
    public boolean keyUp(int keycode) {
        BooleanProperty event = events.nullableEvent(Input.Keys.toString(keycode));

        return null != event && event.set(false);
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO:: Key typed events
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (pointer == 0 && button == Input.Buttons.LEFT) {
            return mouseDown.set(true);
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer == 0 && button == Input.Buttons.LEFT) {
            return mouseDown.set(false);
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer == 0) {
            if (mouseMoved(screenX, screenY)) return true;
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return mousePos.set(owner.translateTouchCoordinates(mousePos.ref().set(screenX, screenY)));
    }

    @Override
    public boolean scrolled(int amount) {
        return scroll.set(amount);
    }
}
