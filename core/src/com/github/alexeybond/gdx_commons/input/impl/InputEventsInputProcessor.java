package com.github.alexeybond.gdx_commons.input.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.github.alexeybond.gdx_commons.game.systems.input.InputSystem;
import com.github.alexeybond.gdx_commons.input.InputEvents;
import com.github.alexeybond.gdx_commons.util.event.Events;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;
import com.github.alexeybond.gdx_commons.util.event.props.IntProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;

/**
 *
 */
class InputEventsInputProcessor implements InputProcessor {
    private final Events<InputEvents> events;
    private final InputEventsImpl owner;

    private final Vec2Property<InputEvents> mousePos;
    private final IntProperty<InputEvents> scroll;

    public InputEventsInputProcessor(Events<InputEvents> events, InputEventsImpl owner) {
        this.events = events;
        this.owner = owner;

        mousePos = events.event("mousePos",
                Vec2Property.<InputEvents>make(Gdx.input.getX(), Gdx.input.getY()));
        scroll = events.event("scroll",
                IntProperty.<InputEvents>make(0));
    }

    @Override
    public boolean keyDown(int keycode) {
        BooleanProperty<InputEvents> event = events.nullableEvent(Input.Keys.toString(keycode));

        return null != event && event.set(owner, true);
    }

    @Override
    public boolean keyUp(int keycode) {
        BooleanProperty<InputEvents> event = events.nullableEvent(Input.Keys.toString(keycode));

        return null != event && event.set(owner, false);
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO:: Key typed events
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return mousePos.set(owner, owner.translateTouchCoordinates(mousePos.ref().set(screenX, screenY)));
    }

    @Override
    public boolean scrolled(int amount) {
        return scroll.set(owner, amount);
    }
}
