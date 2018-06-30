package io.github.alexeybond.partly_solid_bicycle.core.interfaces.event;

import org.jetbrains.annotations.NotNull;

/**
 * Topic is an output end of message/event passing channel.
 *
 * @param <TEvent>
 */
public interface Topic<TEvent> {
    /**
     * Subscribe a listener to this topic.
     *
     * @param listener the listener
     * @return the subscription token
     */
    Object subscribe(@NotNull Listener<? super TEvent> listener);

    /**
     * Unsubscribe a listener from this topic.
     * <p>
     * Call of this method with arguments other than a {@link Listener} instance {@link #subscribe(Listener)}
     * was called for and the token object returned by that call will cause undefined behavior.
     *
     * @param token    the subscription token returned by corresponding {@link #subscribe(Listener)} call
     * @param listener the listener
     */
    void unsubscribe(Object token, @NotNull Listener<? super TEvent> listener);
}
