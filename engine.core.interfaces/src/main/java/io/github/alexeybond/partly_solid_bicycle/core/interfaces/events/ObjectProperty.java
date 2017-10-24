package io.github.alexeybond.partly_solid_bicycle.core.interfaces.events;

public interface ObjectProperty<T, TProp extends ObjectProperty<T, TProp>>
        extends Property<TProp> {
    T getValue();

    void setValue(T value);
}
