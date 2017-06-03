package com.github.alexeybond.gdx_commons.game.systems.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.github.alexeybond.gdx_commons.game.event.Events;
import com.github.alexeybond.gdx_commons.game.event.props.BooleanProperty;
import com.github.alexeybond.gdx_commons.game.event.props.IntProperty;
import com.github.alexeybond.gdx_commons.game.event.props.Vec2Property;

/**
 *
 */
public class InputSystemProcessor implements InputProcessor {
    private final Events<InputSystem> events;
    private final InputSystem system;

    private final Vec2Property<InputSystem> mousePos;
    private final IntProperty<InputSystem> scroll;

    public InputSystemProcessor(Events<InputSystem> events, InputSystem system) {
        this.events = events;
        this.system = system;

        mousePos = events.event("mousePos",
                Vec2Property.<InputSystem>make(Gdx.input.getX(), Gdx.input.getY()));
        scroll = events.event("scroll",
                IntProperty.<InputSystem>make(0));
    }

    @Override
    public boolean keyDown(int keycode) {
        BooleanProperty<InputSystem> event = events.nullableEvent(Input.Keys.toString(keycode));

        if (null != event) {
            return event.set(system, true);
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        BooleanProperty<InputSystem> event = events.nullableEvent(Input.Keys.toString(keycode));

        if (null != event) {
            return event.set(system, false);
        }

        return false;
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
        return mousePos.set(system, system.translateTouchCoordinates(mousePos.ref().set(screenX, screenY)));
    }

    @Override
    public boolean scrolled(int amount) {
        return scroll.set(system, amount);
    }
}
