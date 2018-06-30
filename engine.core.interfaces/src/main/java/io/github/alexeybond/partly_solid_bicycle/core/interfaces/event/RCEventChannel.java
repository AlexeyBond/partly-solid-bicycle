package io.github.alexeybond.partly_solid_bicycle.core.interfaces.event;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.reference_counted.ReferenceCounted;

/**
 * A {@link Channel} that receives {@link ReferenceCounted} objects as events.
 * <p>
 * {@link RCEventChannel} guarantees proper handling of event's reference counter.
 *
 * @param <T>
 */
public interface RCEventChannel<T extends ReferenceCounted>
        extends Channel<T> {
}
