package io.github.alexeybond.partly_solid_bicycle.core.events;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Object that gets notified when event happens.
 *
 * @param <T> event source type
 */
public interface EventListener<T extends EventSource> {
    /**
     * Called when event happens.
     *
     * @param source      event source
     * @param initializer object that caused the event
     */
    void onEvent(@NotNull T source, @Nullable Object initializer);
}
