package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context;

import org.jetbrains.annotations.NotNull;

/**
 * Context of an item processed by the preprocessor - a component, property or something else.
 * <p>
 * All objects related to the item are stored in a key-value storage the context provides access to.
 */
public interface ItemContext {
    <T> T get(@NotNull String id);

    void set(@NotNull String id, @NotNull Object value);

    @NotNull
    ProcessingContext getContext();
}
