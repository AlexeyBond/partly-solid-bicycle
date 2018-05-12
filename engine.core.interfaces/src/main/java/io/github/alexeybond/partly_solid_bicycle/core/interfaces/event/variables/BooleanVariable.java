package io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

/**
 * Boolean variable.
 * <p>
 * Just an {@code ObjectVariable<Boolean>} that provides additional methods working with unboxed value.
 */
public interface BooleanVariable extends ObjectVariable<Boolean> {
    boolean getBoolean();

    void set(boolean value);

    void set(boolean value, @NotNull Executor notificationExecutor);
}
