package io.github.alexeybond.partly_solid_bicycle.core.events.sources;

import io.github.alexeybond.partly_solid_bicycle.core.events.EventListener;
import io.github.alexeybond.partly_solid_bicycle.core.events.EventSource;

/**
 * Source of events that never happen.
 */
public enum NullEventSource implements EventSource {
    INSTANCE;

    @Override
    public int subscribe(EventListener<? extends EventSource> listener) {
        return -1;
    }

    @Override
    public int unsubscribe(int subId, EventListener<? extends EventSource> listener) {
        return -1;
    }
}
