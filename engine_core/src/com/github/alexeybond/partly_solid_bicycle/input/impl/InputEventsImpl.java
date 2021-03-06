package com.github.alexeybond.partly_solid_bicycle.input.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.alexeybond.partly_solid_bicycle.input.InputEvents;
import com.github.alexeybond.partly_solid_bicycle.util.event.Events;
import com.github.alexeybond.partly_solid_bicycle.util.event.DefaultEvents;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.BooleanProperty;

import java.util.ArrayList;
import java.util.Locale;

/**
 *
 */
public class InputEventsImpl implements InputEvents {
    private Events inputEvents = new DefaultEvents();

    private final InputMultiplexer inputMultiplexer;

    private final ArrayList<InputProcessor> processors = new ArrayList<InputProcessor>();
    private boolean enabled = false;

    // Required for mouse/touch coordinates translation
    private final Viewport screenViewport;

    public InputEventsImpl(Viewport screenViewport) {
        this.screenViewport = screenViewport;
        inputMultiplexer = new InputMultiplexer();
        processors.add(new GestureDetector(new InputEventsGestureListener(inputEvents, this)));
        processors.add(new InputEventsInputProcessor(inputEvents, this));
    }

    private void activateProcessors() {
        for (int i = 0; i < processors.size(); i++) {
            inputMultiplexer.addProcessor(processors.get(i));
        }
    }

    private void deactivateProcessors() {
        inputMultiplexer.clear();
    }

    /**
     * Translate screen coordinates to coordinates inside of screen viewport but ignore {@link Viewport#camera} (camera
     * un-projection should be applied later).
     */
    Vector2 translateTouchCoordinates(Vector2 screenCoordinates) {
        screenCoordinates.sub(screenViewport.getScreenX(), screenViewport.getScreenY());
        screenCoordinates.scl(
                1f / (float) screenViewport.getScreenWidth(),
                1f / (float) screenViewport.getScreenHeight());
        screenCoordinates.set(
                MathUtils.clamp(screenCoordinates.x, 0, 1),
                MathUtils.clamp(screenCoordinates.y, 0, 1));
//        screenCoordinates.scl(screenViewport.getWorldWidth(), screenViewport.getWorldHeight());
        return screenCoordinates;
    }

    @Override
    public Events events() {
        return inputEvents;
    }

    @Override
    public InputProcessor inputProcessor() {
        return inputMultiplexer;
    }

    @Override
    public BooleanProperty keyEvent(int code) {
        return keyEvent0(Input.Keys.toString(code), code);
    }

    @Override
    public BooleanProperty keyEvent(String name) {
        return keyEvent0(name, Input.Keys.valueOf(name));
    }

    private BooleanProperty keyEvent0(String keyName, int code) {
        if (null == keyName || -1 == code)
            throw new IllegalArgumentException(
                    String.format(Locale.getDefault(), "Illegal key code/name: %d/%s", code, keyName));

        return inputEvents.event(keyName, BooleanProperty.make(Gdx.input.isKeyPressed(code)));
    }

    @Override
    public void enable() {
        if (enabled) return;

        activateProcessors();

        enabled = true;
    }

    @Override
    public void disable() {
        deactivateProcessors();

        enabled = false;
    }

    @Override
    public void addSlaveProcessor(InputProcessor processor, boolean front) {
        processors.add(front ? 0 : processors.size(), processor);

        if (enabled) {
            deactivateProcessors();
            activateProcessors();
        }
    }

    @Override
    public void removeSlaveProcessor(InputProcessor processor) {
        processors.remove(processor);

        if (enabled) {
            deactivateProcessors();
            activateProcessors();
        }
    }

    @Override
    public InputEvents makeChild() {
        InputEvents child = new InputEventsImpl(screenViewport);
        addSlaveProcessor(child.inputProcessor(), false);
        return child;
    }
}
