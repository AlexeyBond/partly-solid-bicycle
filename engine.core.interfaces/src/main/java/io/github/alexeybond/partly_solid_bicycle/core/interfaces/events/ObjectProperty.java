package io.github.alexeybond.partly_solid_bicycle.core.interfaces.events;

import org.jetbrains.annotations.Nullable;

/**
 * Property which value is representable as a object.
 *
 * @param <T>     object type
 * @param <TProp> full type of the property
 */
public interface ObjectProperty<T, TProp extends ObjectProperty<T, TProp>>
        extends Property<TProp> {
    /**
     * @return current value
     */
    T getValue();

    /**
     * @param value     new value
     * @param initiator object that caused the change
     */
    void setValue(T value, @Nullable Object initiator);
}
