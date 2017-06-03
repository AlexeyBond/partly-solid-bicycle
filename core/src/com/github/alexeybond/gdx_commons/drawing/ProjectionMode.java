package com.github.alexeybond.gdx_commons.drawing;

import com.badlogic.gdx.math.Matrix4;

/**
 *
 */
public interface ProjectionMode {
    void setup(Matrix4 projectionMatrix, float targetWidth, float targetHeight);
}
