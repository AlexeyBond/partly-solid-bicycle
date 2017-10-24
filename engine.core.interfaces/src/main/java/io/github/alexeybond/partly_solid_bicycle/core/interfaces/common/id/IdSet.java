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
     *
     * <p>
     *  Always returns the same id for any equal non-null key objects.
     * </p>
     *
     * @param key
     * @return
     */
    // TODO:: Do not allow null-keys and use separate method for anon-id?
    @NotNull
    Id<T> get(@Nullable Object key);
}
