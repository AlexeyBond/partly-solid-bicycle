package io.github.alexeybond.partly_solid_bicycle.core.events.listeners;

import io.github.alexeybond.partly_solid_bicycle.core.events.EventListener;
import io.github.alexeybond.partly_solid_bicycle.core.events.EventSource;
import io.github.alexeybond.partly_solid_bicycle.core.events.sources.NullEventSource;

/**
 * Helper class managing subscription to single event.
 *
 * <p>
 *  Supposed use scenario:
 * </p>
 * <pre>{@code
 *  // Create subscription
 *  Subscription<EventSource> mySub = new Subscription() {
 *      public void onEvent(EventSource source, Object initializer) {
 *          // ...
 *      }
 *  }
 *
 *  EventSource source = ...;
 *
 *  // Subscribe to event
 *  mySub.subscribe(source);
 *
 *  ...
 *
 *  // Unsubscribe
 *  mySub.clear();
 * }</pre>
 *
 * @param <T> event source type
 */
public abstract class Subscription<T extends EventSource> implements EventListener<T> {
    private EventSource source = NullEventSource.INSTANCE;
    private int subId = -1;

    /**
     * Subscribe to events from given source. Will unsubscribe from any other source is subscribed if any.
     *
     * @param src the event source
     */
    public void subscribe(T src) {
        subId = source.unsubscribe(subId, this);
        subId = src.subscribe(this);
        source = src;
    }

    /**
     * Unsubscribe from any events. Has no effect if is not subscribed to any events.
     */
    public void clear() {
        subId = source.unsubscribe(subId, this);
        source = NullEventSource.INSTANCE;
    }
}
