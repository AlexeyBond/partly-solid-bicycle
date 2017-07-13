package com.github.alexeybond.gdx_commons.util.event;

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
 */
public class Event {
    private EventListener[] listeners;
    private int freeSlotPointer, freeSlotCount;

    public static int DEFAULT_EVENT_SLOTS = 4;

    private static final EventFactoryProvider<EventFactory<Event>> factoryProvider
            = new EventFactoryProvider<EventFactory<Event>>(new EventFactory<Event>() {
        @Override
        public Event create() {
            return new Event(DEFAULT_EVENT_SLOTS);
        }
    });

    protected Event(int initialSlots) {
        listeners = initialSlots == 0 ? null : new EventListener[initialSlots];
        freeSlotPointer = 0;
        freeSlotCount = initialSlots;
    }

    protected Event() {
        this(DEFAULT_EVENT_SLOTS);
    }

    public int subscribe(EventListener<? extends Event> listener) {
        if (freeSlotCount <= 0) {
            if (listeners != null) {
                final int oldLen = listeners.length;
                final int newLen = listeners.length * 2;
                EventListener[] newListeners = new EventListener[newLen];
                System.arraycopy(listeners, 0, newListeners, 0, oldLen);
                freeSlotCount += (newLen - oldLen);
                listeners = newListeners;
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

    public boolean trigger() {
        if (null == listeners) return false;

        boolean processed = false;

        for (int i = 0; i < listeners.length; i++) {
            EventListener listener = listeners[i];

            if (null != listener) {
                boolean processedInt = listener.onTriggered(this);
                processed = processed || processedInt;
            }
        }

        return processed;
    }

    public static EventFactory<Event> makeEvent() {
        return factoryProvider.get();
    }
}
