package com.github.alexeybond.partly_solid_bicycle.util.event;

import com.github.alexeybond.partly_solid_bicycle.util.event.exception.NoSuchEventException;

/**
 * Interface for collection of named {@link Event event}'s.
 */
public interface Events {
    /**
     * Get event with given name or create it using given factory.
     *
     * @param name    event name
     * @param factory factory to create event
     * @param <T>     expected event type
     * @return the exist or created event
     * @throws RuntimeException if event factory throws
     */
    <T extends Event> T event(String name, EventFactory<T> factory) throws RuntimeException;

    /**
     * Get event with given name.
     *
     * @param name event name
     * @param <T>  expected event type
     * @return the event
     * @throws NoSuchEventException if there is no event with given name
     */
    <T extends Event> T event(String name) throws NoSuchEventException;

    /**
     * Get event with given name.
     *
     * @param name event name
     * @param <T>  expected event type
     * @return the event or {@code null} if no event with given name found
     */
    <T extends Event> T nullableEvent(String name);
}
