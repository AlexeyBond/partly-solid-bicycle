package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render;

import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.batching.BatchingState;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.RenderTarget;
import org.jetbrains.annotations.NotNull;

public interface DrawingState {
    /**
     * @return the batching state
     */
    @NotNull
    BatchingState batching();

    /**
     * Get a target the scene should be rendered to.
     *
     * @return the output target
     */
    @NotNull
    RenderTarget getOutputTarget();

    /**
     * Get a {@link RenderTarget} the rendering is currently performed to.
     *
     * @return the current target
     */
    @NotNull
    RenderTarget getCurrentTarget();

    /**
     * Begin rendering to given target.
     *
     * @param target the target
     */
    void beginTo(@NotNull RenderTarget target);
}
