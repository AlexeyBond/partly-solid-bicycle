package io.github.alexeybond.partly_solid_bicycle.game2d.render.batching;

public interface BatchingMode<T> {
    /**
     * @throws ClassCastException if state type does not match the expected one
     */
    T begin(BatchingState state);
}
