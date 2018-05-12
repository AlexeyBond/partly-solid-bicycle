package io.github.alexeybond.partly_solid_bicycle.core.interfaces.event;

/**
 * Variable is an object holding a value and providing events happening when the value changes.
 *
 * @param <T>
 */
public interface Variable<T extends Variable<T>> extends StateTopic<T> {
}
