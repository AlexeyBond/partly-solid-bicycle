package io.github.alexeybond.partly_solid_bicycle.core.common.scope;

public interface Factory<T, A> {
    T create(A arg);
}
