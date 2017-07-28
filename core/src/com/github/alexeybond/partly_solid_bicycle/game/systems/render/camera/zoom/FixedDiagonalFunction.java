package com.github.alexeybond.partly_solid_bicycle.game.systems.render.camera.zoom;

import com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.ZoomFunction;

/**
 * Zoom function that keeps a constant virtual screen diagonal.
 */
public class FixedDiagonalFunction implements ZoomFunction {
    private final float targetDiagonal;

    public FixedDiagonalFunction(float targetDiagonal) {
        this.targetDiagonal = targetDiagonal;
    }

    @Override
    public float compute(float width, float height) {
        float currentDiagonal = (float) Math.sqrt(width * width + height * height);

        return currentDiagonal / targetDiagonal;
    }
}
