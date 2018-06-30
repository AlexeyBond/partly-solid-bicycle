package io.github.alexeybond.partly_solid_bicycle.core.impl.event.variable;

import io.github.alexeybond.partly_solid_bicycle.core.impl.common.executors.ImmediateSynchronousExecutor;
import io.github.alexeybond.partly_solid_bicycle.core.impl.event.notifier.ArrayNotifier;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables.MutableObjectVariable;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables.ObjectVariable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class DefaultMutableObjectVariable<T>
        extends ArrayNotifier<ObjectVariable<T>>
        implements MutableObjectVariable<T>, Runnable {
    private static final Executor DEFAULT_EXECUTOR = ImmediateSynchronousExecutor.INSTANCE;

    @NotNull
    private T current, next;
    private boolean dirty = false;
    private boolean notifying = false;

    public DefaultMutableObjectVariable(
            Listener<ObjectVariable<T>>[] subArray,
            @NotNull T current,
            @NotNull T next) {
        super(subArray);
        this.current = current;
        this.next = next;
    }

    public DefaultMutableObjectVariable(
            int capacity,
            @NotNull T current,
            @NotNull T next) {
        super(capacity);
        this.current = current;
        this.next = next;
    }

    @Override
    public T mutable() {
        return next;
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
        if (notifying) return;

        try {
            notifying = true;

            while (dirty) {
                T cur = current;
                current = next;
                next = cur;
                dirty = false;

                notifyListeners(this, this);
            }
        } finally {
            notifying = false;
        }
    }
}
