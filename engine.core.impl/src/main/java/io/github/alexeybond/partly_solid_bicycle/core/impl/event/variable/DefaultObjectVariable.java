package io.github.alexeybond.partly_solid_bicycle.core.impl.event.variable;

import io.github.alexeybond.partly_solid_bicycle.core.impl.common.executors.ImmediateSynchronousExecutor;
import io.github.alexeybond.partly_solid_bicycle.core.impl.event.notifier.ArrayNotifier;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables.ObjectVariable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class DefaultObjectVariable<T>
        extends ArrayNotifier<ObjectVariable<T>>
        implements ObjectVariable<T>, Runnable {
    private static final Executor DEFAULT_EXECUTOR = ImmediateSynchronousExecutor.INSTANCE;

    private T current;
    private T next;
    private boolean dirty = false;
    private boolean notifying = false;

    protected DefaultObjectVariable(int capacity) {
        super(capacity);
    }

    @Override
    public T get() {
        return current;
    }

    @Override
    public void set(T value) {
        set(value, DEFAULT_EXECUTOR);
    }

    @Override
    public void set(T value, @NotNull Executor notificationExecutor) {
        next = value;
        dirty = true;

        if (notifying) return;

        notificationExecutor.execute(this);
    }

    @Override
    public void pull(@NotNull Listener<? super ObjectVariable<T>> listener) {
        listener.receive(this, this);
    }

    @Override
    public void run() {
        try {
            notifying = true;

            while (dirty) {
                dirty = false;
                current = next;

                notifyListeners(this, this);
            }
        } finally {
            notifying = false;
        }
    }
}
