package io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Variable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

/**
 * A {@link Variable} which value is representable as an object.
 *
 * @param <T> value type
 */
public interface ObjectVariable<T> extends Variable<ObjectVariable<T>> {
    /**
     * Get current value of this variable.
     * <p>
     * Any changes to state of the returned object will cause undefined behavior.
     *
     * @return current value of this variable
     */
    T get();

    /**
     * Set new value.
     * <p>
     * Call of this method will cause notification of all
     * {@link io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener listeners}
     * subscribed to this variable.
     *
     * @param value the new value
     */
    void set(T value);

    /**
     * Set new value.
     * <p>
     * This method is alike to {@link #set(Object)} but allows to provide the {@link Executor executor}
     * to be used to notify listeners.
     *
     * @param value                the new value
     * @param notificationExecutor executor to use to notify listeners
     */
    void set(T value, @NotNull Executor notificationExecutor);
}
