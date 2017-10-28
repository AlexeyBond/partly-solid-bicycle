package io.github.alexeybond.partly_solid_bicycle.core.impl.events.sources;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventListener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventSource;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for non-thread-safe {@link EventSource event sources}.
 *
 * @param <T>
 */
public abstract class DefaultEventSource<T extends EventSource> implements EventSource<T> {
    private static EventListener[] NO_LISTENERS = new EventListener[0];

    private EventListener<T>[] listeners;
    private int firstCheckFree, freeCount;

    protected DefaultEventSource(EventListener<T>[] subArray) {
        this.firstCheckFree = 0;
        this.freeCount = subArray.length;
        this.listeners = subArray;
    }

    @SuppressWarnings({"unchecked"})
    protected DefaultEventSource(int capacity) {
        this(0 == capacity ? NO_LISTENERS : new EventListener[capacity]);
    }

    private int unsubscribe0(EventListener<T>[] listeners, int uSubId) {
        listeners[uSubId] = null;
        if (uSubId < firstCheckFree) {
            firstCheckFree = uSubId;
        }
        ++freeCount;
        return -1;
    }

    private int allocIndex() {
        EventListener<T>[] listeners = this.listeners;

        if (freeCount <= 0) {
            int newCapacity = Math.max(listeners.length * 2, 4);

            @SuppressWarnings({"unchecked"})
            EventListener<T>[] newListeners = (EventListener<T>[]) new EventListener[newCapacity];

            System.arraycopy(listeners, 0, newListeners, 0, listeners.length);

            this.freeCount = newListeners.length - listeners.length;
            this.firstCheckFree = listeners.length;
            listeners = newListeners;
            this.listeners = listeners;
        }

        int freeId;

        // firstCheckFree is always first free index or index before the first free index
        // so it's OK to ignore range [0, firstCheckFree)
        for (freeId = firstCheckFree; freeId < listeners.length; ++freeId) {
            if (null == listeners[freeId]) {
                firstCheckFree = freeId + 1;
                break;
            }
        }

        return freeId;
    }

    protected void trigger(T origin, Object initiator) {
        EventListener<T>[] listeners = this.listeners;
        int left = listeners.length - freeCount;

        for (int i = 0; left > 0; ++i) {
            EventListener<T> listener = listeners[i];

            if (null != listener) {
                listener.onEvent(origin, initiator);
                --left;
            }
        }
    }

    @Override
    public int subscribe(@NotNull EventListener<T> listener) {
        int id = allocIndex();
        --freeCount;
        listeners[id] = listener;
        return id;
    }

    @Override
    public int unsubscribe(int subId, @NotNull EventListener<T> listener) {
        if (subId < 0) return subId;

        EventListener<T>[] listeners = this.listeners;

        if (listeners.length <= subId || listeners[subId] != listener) {
//            for (int i = 0; i < subId; ++i)
//                if (listeners[i] == listener)
//                    return unsubscribe0(listeners, i);

            return -1;
        } else {
            return unsubscribe0(listeners, subId);
        }
    }
}
