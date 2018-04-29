package io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables;

/**
 * The {@link ObjectVariable} which state is a mutable object.
 *
 * @param <T>
 */
public interface MutableObjectVariable<T> extends ObjectVariable<T> {
    /**
     * Get a object that may be used as new state for this variable.
     * <p>
     * The returned object may but is not guaranteed to be the same as one returned by {@link #get()}.
     *
     * @return mutable object that may be used as new value for this variable
     */
    T mutable();
}
