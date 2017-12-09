package io.github.alexeybond.partly_solid_bicycle.game2d.render.batching;

import com.badlogic.gdx.math.Matrix4;
import org.jetbrains.annotations.NotNull;

/**
 * Component managing geometry batching mechanisms.
 */
public interface BatchingState {
    /**
     * Flush all batched geometry so any changes to GL state can be made without affecting it.
     */
    void flush();

    /**
     * Begin batched drawing in given mode.
     * <p>
     * <p>
     * For non-composite {@link BatchingState state} {@code state}
     * <pre>
     *         x = state.begin(mode);
     *     </pre>
     * <p>
     * should be equivalent to
     * <pre>
     *         x = mode.begin(state);
     *     </pre>
     * </p>
     */
    <T> T begin(@NotNull BatchingMode<T> mode);

    /**
     * Flush all batched geometry and stop geometry batching.
     */
    void end();

    /**
     * Set projection matrix applied to all batched geometry.
     */
    void setProjection(@NotNull Matrix4 projectionMatrix);
}
