package io.github.alexeybond.partly_solid_bicycle.core.interfaces.events;

public interface Property<T extends Property<T>> extends EventSource<T> {
    void read(Object data);

    Object serialize();
}
