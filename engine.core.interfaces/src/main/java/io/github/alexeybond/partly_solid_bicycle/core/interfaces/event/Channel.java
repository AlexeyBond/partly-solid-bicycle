package io.github.alexeybond.partly_solid_bicycle.core.interfaces.event;

import org.jetbrains.annotations.NotNull;

/**
 * Channel is a "receiving end" of a topic that allows events to be published on it.
 *
 * @param <T>
 */
public interface Channel<T> {
    void send(@NotNull T event);
}
