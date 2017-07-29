package com.github.alexeybond.partly_solid_bicycle.game.systems.render.camera.zoom;

import com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.ZoomFunction;

/**
 * Returns zoom of 1.0 at any resolution i.e. screen with resolution WxH will show world region of size WxH.
 */
public enum UnitZoomFunction implements ZoomFunction {
    INSTANCE;

    @Override
    public float compute(float width, float height) {
        return 1;
    }
}
