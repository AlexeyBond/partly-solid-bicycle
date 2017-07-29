package com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces;

/**
 * Function that computes camera zoom for specified screen width and height.
 *
 * When zoom is 1.0 a camera shows a region of the world of the same size as size of virtual screen.
 */
public interface ZoomFunction {
    /**
     * @param width     virtual width of screen
     * @param height    virtual height of screen
     * @return camera zoom
     */
    float compute(float width, float height);
}
