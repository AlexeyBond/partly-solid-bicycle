package com.github.alexeybond.gdx_commons.util.event;

import java.util.Arrays;

/**
 *
 * <pre>{@code
 *
 *      Event<T> event = new Event<T>();
 *
 *      // Subscribe:
 *      int subscription = event.subscribe(listener);
 *
 *      // Unsubscribe:
 *      subscription = event.unsubscribe(subscription);
 *
 *      // Trigger:
 *      event.trigger(initiator);
 * }</pre>
 *
 * @param <TInitiator> type of object initializing the event
 */
public class Event<TInitiator> {
    private EventListener[] listeners;
    private int freeSlotPointer, freeSlotCount;

    public static int DEFAULT_EVENT_SLOTS = 4;

    public Event(int initialSlots) {
        listeners = initialSlots == 0 ? null : new EventListener[initialSlots];
        freeSlotPointer = 0;
        freeSlotCount = initialSlots;
    }

    public Event() {
        this(DEFAULT_EVENT_SLOTS);
    }

    public int subscribe(EventListener<TInitiator, ? extends Event<TInitiator>> listener) {
        if (freeSlotCount <= 0) {
            if (listeners != null) {
                int oldLen = listeners.length;
                listeners = Arrays.copyOf(listeners, oldLen * 2);
                freeSlotCount += oldLen;
            } else {
                listeners = new EventListener[DEFAULT_EVENT_SLOTS];
                freeSlotCount = DEFAULT_EVENT_SLOTS;
            }
        }

        --freeSlotPointer;

        do {
            ++freeSlotPointer;

            if (freeSlotPointer >= listeners.length) {
                freeSlotPointer = 0;
            }
        } while (null != listeners[freeSlotPointer]);

        --freeSlotCount;

        listeners[freeSlotPointer] = listener;

        return freeSlotPointer++;
    }

    public int unsubscribe(int subscriptionIdx) {
        if (subscriptionIdx >= 0) {
            listeners[subscriptionIdx] = null;
        }

        return -1;
    }

    public boolean trigger(TInitiator initiator) {
        if (null == listeners) return false;

        boolean processed = false;

        for (int i = 0; i < listeners.length; i++) {
            EventListener listener = listeners[i];

            if (null != listener) {
                boolean processedInt = listener.onTriggered(initiator, this);
                processed = processed || processedInt;
            }
        }

        return processed;
    }
}
