package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target;

import org.jetbrains.annotations.NotNull;

/**
 * A strategy computing viewport parameters using underlying target sizes.
 */
public interface ViewportSizingStrategy {
    /**
     * Configure the given viewport.
     *
     * @param viewport the viewport to configure
     * @param target   master target (the target which rectangle is represented by configurable viewport)
     */
    void setup(@NotNull Viewport viewport, @NotNull RenderTarget target);
}
