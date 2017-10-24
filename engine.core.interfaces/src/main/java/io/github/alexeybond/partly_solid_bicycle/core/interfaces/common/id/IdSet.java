package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Set of {@link Id identifiers}.
 *
 * @param <T> type of identifiable object
 */
public interface IdSet<T> {
    /**
     * Create a identifier associated with given key object.
     *
     * <p>
     *  Always returns the same id for any equal key objects.
     * </p>
     *
     * @param key the object to associate id with. Usually a {@link String}.
     * @return id
     */
    @NotNull
    Id<T> get(@NotNull Object key);

    /**
     * Create a identifier that is not associated with any key object. Such identifiers are useful to identify
     * unnamed or dynamically created objects.
     *
     * @return id
     */
    @NotNull
    Id<T> unnamed();
}
