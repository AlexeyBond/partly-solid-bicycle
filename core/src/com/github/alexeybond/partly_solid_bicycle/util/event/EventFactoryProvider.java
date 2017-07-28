package com.github.alexeybond.partly_solid_bicycle.util.event;

// TODO:: Use thread-local factories if events will have be able to operate in concurrent environment
public class EventFactoryProvider <T extends EventFactory> {
    private T factory;

    public EventFactoryProvider(T factory) {
        this.factory = factory;
    }

    public T get() {
        return factory;
    }
}
