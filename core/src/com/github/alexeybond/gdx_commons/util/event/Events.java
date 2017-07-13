package com.github.alexeybond.gdx_commons.util.event;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 */
public class Events {
    private final Map<String, Event> events = new HashMap<String, Event>();

    public <T extends Event> T event(String name, EventFactory<T> factory) {
        Event present = events.get(name);
        if (null != present) return (T) present;

        T event = factory.create();
        events.put(name, event);
        return event;
    }

    public <T extends Event> T event(String name) {
        T event = nullableEvent(name);

        if (null == event) {
            throw new NoSuchElementException("No such event: " + name);
        }

        return event;
    }

    public <T extends Event> T nullableEvent(String name) {
        return (T) events.get(name);
    }
}
