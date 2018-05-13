package io.github.alexeybond.partly_solid_bicycle.core.interfaces.event;

import org.jetbrains.annotations.NotNull;

/**
 * Listener is the object able to receive messages from one or more {@link Topic}.
 *
 * @param <TEvent> type of received messages
 */
public interface Listener<TEvent> {
    /**
     * Receive an event.
     *
     * @param event the event
     * @param topic the topic
     */
    void receive(@NotNull TEvent event, @NotNull Topic<? extends TEvent> topic);
}
