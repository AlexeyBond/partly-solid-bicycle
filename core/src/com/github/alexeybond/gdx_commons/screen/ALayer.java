package com.github.alexeybond.gdx_commons.screen;

import com.badlogic.gdx.InputProcessor;

/**
 *
 */
public class ALayer {
    private final AScreen screen;

    protected ALayer(AScreen screen) {
        this.screen = screen;
    }

    public final AScreen screen() {
        return screen;
    }

    public InputProcessor getInputProcessor() {
        return null;
    }

    /**
     * @param dt    time delta in seconds
     */
    public void update(float dt) {

    }
}
