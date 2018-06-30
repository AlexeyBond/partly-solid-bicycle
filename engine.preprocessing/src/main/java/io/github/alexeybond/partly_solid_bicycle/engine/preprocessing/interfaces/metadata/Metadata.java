package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.metadata;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An immutable key-value storage associated with a class/property/method.
 * <p>
 * Metadata content is usually defined by
 * {@link io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Meta @Meta}
 * annotations the element is annotated with.
 */
public interface Metadata {
    @Nullable
    String get(@NotNull String key);
}
