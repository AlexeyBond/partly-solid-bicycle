package io.github.alexeybond.partly_solid_bicycle.core.impl.event.variable;

import io.github.alexeybond.partly_solid_bicycle.core.impl.common.executors.ImmediateSynchronousExecutor;
import io.github.alexeybond.partly_solid_bicycle.core.impl.event.notifier.ArrayNotifier;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables.BooleanVariable;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables.ObjectVariable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class DefaultBooleanVariable
        extends ArrayNotifier<ObjectVariable<Boolean>>
        implements BooleanVariable, Runnable {
    private static final Executor DEFAULT_EXECUTOR = ImmediateSynchronousExecutor.INSTANCE;

    private boolean current = false, next = false;
    private boolean notifying;

    protected DefaultBooleanVariable(Listener<ObjectVariable<Boolean>>[] subArray) {
        super(subArray);
    }

    protected DefaultBooleanVariable(int capacity) {
        super(capacity);
    }

    @Override
    public boolean getBoolean() {
        return current;
    }

    @Override
    public void set(boolean value) {
        set(value, DEFAULT_EXECUTOR);
    }

    @Override
    public void set(boolean value, @NotNull Executor notificationExecutor) {
        next = value;

        if (notifying || next == current) return;

        notificationExecutor.execute(this);
    }

    @Override
    public Boolean get() {
        return current;
    }

    @Override
    public void set(Boolean value) {
        set(value, DEFAULT_EXECUTOR);
    }

    @Override
    public void set(Boolean value, @NotNull Executor notificationExecutor) {
        set(value.booleanValue(), notificationExecutor);
    }

    @Override
    public void pull(@NotNull Listener<? super ObjectVariable<Boolean>> listener) {
        listener.receive(this, this);
    }

    @Override
    public void run() {
        if (notifying) return;

        try {
            notifying = true;

            while (next != current) {
                current = next;

                notifyListeners(this, this);
            }
        } finally {
            notifying = false;
        }
    }
}
