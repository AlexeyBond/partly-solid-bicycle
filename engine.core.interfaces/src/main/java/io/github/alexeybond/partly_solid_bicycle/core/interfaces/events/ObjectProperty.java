package io.github.alexeybond.partly_solid_bicycle.core.interfaces.events;

public interface ObjectProperty<T> extends Property {
    T getValue();

    void setValue(T value);
}
