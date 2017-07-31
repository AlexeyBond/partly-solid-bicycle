package com.github.alexeybond.partly_solid_bicycle.util.event;

import com.github.alexeybond.partly_solid_bicycle.util.event.exception.NoSuchEventException;

import java.util.HashMap;
import java.util.Map;

public class DefaultEvents implements Events {
    private final Map<String, Event> events = new HashMap<String, Event>();

    @Override
    public <T extends Event> T event(String name, EventFactory<T> factory) {
        Event present = events.get(name);
        if (null != present) return (T) present;

        T event = factory.create();
        events.put(name, event);
        return event;
    }

    @Override
    public <T extends Event> T event(String name) {
        T event = nullableEvent(name);

        if (null == event) {
            throw new NoSuchEventException(name);
        }

        return event;
    }

    @Override
    public <T extends Event> T nullableEvent(String name) {
        return (T) events.get(name);
    }
}
