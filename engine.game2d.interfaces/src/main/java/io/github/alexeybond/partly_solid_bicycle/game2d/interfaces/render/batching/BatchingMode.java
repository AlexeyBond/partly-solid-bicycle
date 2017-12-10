package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.batching;

import org.jetbrains.annotations.NotNull;

public interface BatchingMode<T> {
    /**
     * @throws ClassCastException if state type does not match the expected one
     */
    T begin(@NotNull BatchingState state);
}
