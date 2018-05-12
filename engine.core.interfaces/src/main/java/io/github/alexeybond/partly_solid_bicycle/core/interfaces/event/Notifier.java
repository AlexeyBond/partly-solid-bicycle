package io.github.alexeybond.partly_solid_bicycle.core.interfaces.event;

import org.jetbrains.annotations.NotNull;

/**
 * Notifier is an object managing list of {@link Topic} subscribers and sending notifications when necessary.
 * <p>
 * Notifier usually is just a part of {@link Topic} implementation not available publicly.
 *
 * @param <T>
 */
public interface Notifier<T>
        extends Topic<T> {
    void notifyListeners(T value, @NotNull Topic<T> topic);
}
