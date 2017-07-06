package com.github.alexeybond.gdx_commons.game.systems.render.camera.zoom;

import com.github.alexeybond.gdx_commons.game.systems.render.interfaces.ZoomFunction;

/**
 *
 */
public class ConstantZoomFunction implements ZoomFunction {
    private final float zoom;

    public ConstantZoomFunction(float zoom) {
        this.zoom = zoom;
    }

    @Override
    public float compute(float width, float height) {
        return zoom;
    }
}
