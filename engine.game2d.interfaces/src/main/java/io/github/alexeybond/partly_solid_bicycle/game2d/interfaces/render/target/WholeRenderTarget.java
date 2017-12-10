package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target;

import org.jetbrains.annotations.NotNull;

/**
 * A {@link RenderTarget} that represents a whole area of underlying surface.
 */
public interface WholeRenderTarget extends RenderTarget {
    /**
     * @return 0
     */
    @Override
    int getAbsOffsetX();

    /**
     * @return 0
     */
    @Override
    int getAbsOffsetY();

    /**
     * @return {@code this}
     */
    @NotNull
    @Override
    WholeRenderTarget getUnderlyingTarget();

    /**
     * Does the same as {@link #begin()} but is not guaranteed to setup a viewport.
     *
     * <p>
     * This method is assumed to be called by a {@link RenderTarget} decorator that represents
     * a sub-area of this target inside of {@link #begin()} method before viewport setup.
     * Addition of this method allows to minimize amount of viewport setup calls.
     * </p>
     */
    void begin0();
}
