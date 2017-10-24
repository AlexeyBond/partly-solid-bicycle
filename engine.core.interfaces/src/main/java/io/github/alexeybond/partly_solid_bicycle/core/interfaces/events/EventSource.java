package io.github.alexeybond.partly_solid_bicycle.core.interfaces.events;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.exceptions.NoEventOriginException;
import org.jetbrains.annotations.NotNull;

/**
 * Source of events happening at event source of type {@code TSrc}.
 *
 * @param <TSrc> type of actual events source
 */
public interface EventSource<TSrc extends EventSource> {
    /**
     * Get original source of events produced by this source.
     *
     * <p>
     *  This method throws {@link NoEventOriginException} in some cases:
     *  <ul>
     *      <li>when this source is a stub that does not emit any events ever</li>
     *      <li>when this source is a proxy and does not refer to one and only one source</li>
     *      <li>when this source is proxy and it's underlying source throws</li>
     *  </ul>
     * </p>
     *
     * @return actual source of events
     * @throws NoEventOriginException if source is not available
     */
    @NotNull
    TSrc origin() throws NoEventOriginException;

    /**
     * Subscribe the given listener to events of this source.
     *
     * @param listener the listener
     * @return subscription index or {@code -1} if listener was not subscribed to any events
     */
    int subscribe(@NotNull EventListener<TSrc> listener);

    /**
     * Unsubscribe the given listener from events of this source.
     *
     * <p>
     *  It seems to be more efficient to use method's return value to reset variable like this:
     *  <pre>
     *      int subIdx;
     *      ...
     *      subIdx = src.unsubscribe( ... );
     *  </pre>
     *  than use a literal value like this:
     *  <pre>
     *      src.unsubscribe( ... );
     *      subIdx = -1;
     *  </pre>
     *  as first variant produces less bytecode and thus saves a bit of class file size
     *  and a bit of execution time (at least in interpreted mode).
     * </p>
     *
     * @param subId    subscription identifier returned by matching {@link #subscribe(EventListener)} call
     * @param listener the listener
     * @return subscription index that may be used as a stub meaning "no subscription", usually {@code -1}
     */
    int unsubscribe(int subId, @NotNull EventListener<TSrc> listener);
}
