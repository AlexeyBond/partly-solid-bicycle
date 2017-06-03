package com.github.alexeybond.gdx_commons.game.systems.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.GameSystem;
import com.github.alexeybond.gdx_commons.game.event.Event;
import com.github.alexeybond.gdx_commons.game.event.Events;
import com.github.alexeybond.gdx_commons.game.event.props.BooleanProperty;

/**
 *
 */
public class InputSystem implements GameSystem {
    private Events<InputSystem> inputEvents = new Events<InputSystem>();

    private final InputProcessor rawProcessor;
    private final InputProcessor gestureProcessor;
    private final InputMultiplexer inputMultiplexer;

    public InputSystem() {
        inputMultiplexer = new InputMultiplexer();
        rawProcessor = new InputSystemProcessor(inputEvents, this);
        gestureProcessor = new GestureDetector(new InputSystemGestureListener(inputEvents, this));
    }

    private void activateProcessors() {
        inputMultiplexer.addProcessor(gestureProcessor);
        inputMultiplexer.addProcessor(rawProcessor);
    }

    private void deactivateProcessors() {
        inputMultiplexer.clear();
    }

    @Override
    public void onConnect(Game game) {
        activateProcessors();
    }

    @Override
    public void onDisconnect(Game game) {
        deactivateProcessors();
    }

    @Override
    public void update(float deltaTime) {

    }

    public InputProcessor inputProcessor() {
        return inputMultiplexer;
    }

    public BooleanProperty<InputSystem> keyEvent(int code) {
        return keyEvent0(Input.Keys.toString(code), code);
    }

    public BooleanProperty<InputSystem> keyEvent(String name) {
        return keyEvent0(name, Input.Keys.valueOf(name));
    }

    private BooleanProperty<InputSystem> keyEvent0(String keyName, int code) {
        if (null == keyName || -1 == code)
            throw new IllegalArgumentException(String.format("Illegal key code/name: %d/%s", code, keyName));

        return inputEvents.event(keyName, BooleanProperty.<InputSystem>make(Gdx.input.isKeyPressed(code)));
    }

    public <T extends Event<InputSystem>> T event(String name) {
        return inputEvents.event(name);
    }
}
