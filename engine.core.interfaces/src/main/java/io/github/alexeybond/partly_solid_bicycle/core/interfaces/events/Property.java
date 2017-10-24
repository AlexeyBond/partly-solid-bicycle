package io.github.alexeybond.partly_solid_bicycle.core.interfaces.events;

public interface Property extends EventSource {
    void read(Object data);

    Object serialize();
}
