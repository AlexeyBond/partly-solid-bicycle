package com.github.alexeybond.partly_solid_bicycle.game.systems.render.camera.zoom;

import com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.ZoomFunction;

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
