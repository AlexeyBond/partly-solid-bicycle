package io.github.alexeybond.partly_solid_bicycle.core.impl.events.sources;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventListener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventSource;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.exceptions.NoEventOriginException;
import org.jetbrains.annotations.NotNull;

/**
 * Source of events that never happen.
 */
public enum NullEventSource implements EventSource {
    INSTANCE;

    @NotNull
    @Override
    public EventSource origin() {
        throw new NoEventOriginException("Null source has no origin.");
    }

    @Override
    public int subscribe(@NotNull EventListener listener) {
        return -1;
    }

    @Override
    public int unsubscribe(int subId, @NotNull EventListener listener) {
        return -1;
    }

    @SuppressWarnings({"unchecked"})
    public static <T extends EventSource> EventSource<T> get() {
        return (EventSource<T>) INSTANCE;
    }
}
