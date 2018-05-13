package io.github.alexeybond.partly_solid_bicycle.core.impl.event.notifier;

import io.github.alexeybond.partly_solid_bicycle.core.impl.util.ExceptionAccumulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Notifier;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;
import org.jetbrains.annotations.NotNull;

public class ArrayNotifier<TEvent> implements Notifier<TEvent> {
    private static Listener[] NO_LISTENERS = new Listener[0];

    private Listener[] listeners;
    private int firstCheckFree, freeCount;

    protected ArrayNotifier(Listener<TEvent>[] subArray) {
        this.firstCheckFree = 0;
        this.freeCount = subArray.length;
        this.listeners = subArray;
    }

    @SuppressWarnings({"unchecked"})
    protected ArrayNotifier(int capacity) {
        this(0 == capacity ? NO_LISTENERS : new Listener[capacity]);
    }

    private void unsubscribe0(Listener[] listeners, int uSubId) {
        listeners[uSubId] = null;
        if (uSubId < firstCheckFree) {
            firstCheckFree = uSubId;
        }
        ++freeCount;
    }

    private int allocIndex() {
        Listener[] listeners = this.listeners;

        if (freeCount <= 0) {
            int newCapacity = Math.max(listeners.length * 2, 4);

            Listener[] newListeners = new Listener[newCapacity];

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

    @Override
    public Object subscribe(@NotNull Listener<? super TEvent> listener) {
        int id = allocIndex();
        --freeCount;
        listeners[id] = listener;
        return id;
    }

    @Override
    public void unsubscribe(Object token, @NotNull Listener<? super TEvent> listener) {
        int subId = (Integer) token;

        Listener[] listeners = this.listeners;

        if (listeners.length > subId && listeners[subId] == listener) {
            unsubscribe0(listeners, subId);
        }
    }

    @Override
    public void notifyListeners(@NotNull TEvent event, @NotNull Topic<TEvent> topic) {
        Listener[] listeners = this.listeners;
        int left = listeners.length - freeCount;

        Throwable acc = ExceptionAccumulator.init();

        for (int i = 0; left > 0; ++i) {
            Listener listener = listeners[i];

            if (null != listener) {
                try {
                    listener.receive(event, topic);
                } catch (Exception e) {
                    acc = ExceptionAccumulator.add(acc, e);
                }
                --left;
            }
        }

        ExceptionAccumulator.<RuntimeException>flush(acc);
    }
}
