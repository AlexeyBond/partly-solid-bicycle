package com.github.alexeybond.partly_solid_bicycle.drawing;

import com.badlogic.gdx.math.Matrix4;

/**
 *
 */
public interface ProjectionMode {
    void setup(Matrix4 projectionMatrix, float targetWidth, float targetHeight);
}
