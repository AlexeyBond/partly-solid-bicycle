package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.mutation;

import org.jetbrains.annotations.NotNull;

public interface Mutation<T> {
    void apply(@NotNull T instance);
}
