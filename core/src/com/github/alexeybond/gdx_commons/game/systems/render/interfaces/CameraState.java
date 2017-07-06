package com.github.alexeybond.gdx_commons.game.systems.render.interfaces;

import com.badlogic.gdx.math.Vector2;

/**
 *
 */
public interface CameraState {
    Vector2 position();

    float rotation();

    ZoomFunction zoomFunction();
}
