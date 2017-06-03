package com.github.alexeybond.gdx_commons.game.event;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Events<TInitiator> {
    private final Map<String, Event<TInitiator>> events = new HashMap<String, Event<TInitiator>>();

    public <T extends Event<TInitiator>> T event(String name, T event) {
        Event<TInitiator> present = events.get(name);

        if (null != present) return (T) present;

        events.put(name, event);

        return event;
    }

    public <T extends Event<TInitiator>> T event(String name) {
        T event = nullableEvent(name);

        if (null == event) {
            throw new IllegalArgumentException("No such event: " + name);
        }

        return event;
    }

    public <T extends Event<TInitiator>> T nullableEvent(String name) {
        return (T) events.get(name);
    }
}
