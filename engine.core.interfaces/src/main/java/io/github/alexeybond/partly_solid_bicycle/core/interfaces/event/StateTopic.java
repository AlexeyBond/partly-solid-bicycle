package io.github.alexeybond.partly_solid_bicycle.core.interfaces.event;

import org.jetbrains.annotations.NotNull;

/**
 * Topic that supplies events about change of state of some object.
 *
 * @param <TState> type of a state of the observable object
 */
public interface StateTopic<TState> extends Topic<TState> {
    /**
     * Notify given listener on current state of observable object.
     *
     * @param listener the listener to notify
     */
    void pull(@NotNull Listener<? super TState> listener);
}
