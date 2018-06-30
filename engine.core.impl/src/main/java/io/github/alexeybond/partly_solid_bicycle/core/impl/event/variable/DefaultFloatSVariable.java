package io.github.alexeybond.partly_solid_bicycle.core.impl.event.variable;

import io.github.alexeybond.partly_solid_bicycle.core.impl.common.executors.ImmediateSynchronousExecutor;
import io.github.alexeybond.partly_solid_bicycle.core.impl.event.notifier.ArrayNotifier;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables.FloatVariable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

/**
 * Implementation of {@link FloatVariable} that stores a single-precision float value.
 */
public class DefaultFloatSVariable
        extends ArrayNotifier<FloatVariable>
        implements FloatVariable, Runnable {
    private static final Executor DEFAULT_EXECUTOR = ImmediateSynchronousExecutor.INSTANCE;

    private float current, next;
    private boolean dirty = false;
    private boolean notifying = false;

    protected DefaultFloatSVariable(Listener<FloatVariable>[] subArray) {
        super(subArray);
    }

    protected DefaultFloatSVariable(int capacity) {
        super(capacity);
    }

    @Override
    public float getFloat() {
        return current;
    }

    @Override
    public double getDouble() {
        return (double) current;
    }

    @Override
    public void set(float value) {
        next = value;

        postChange(DEFAULT_EXECUTOR);
    }

    @Override
    public void set(float value, Executor notificationExecutor) {
        next = value;

        postChange(notificationExecutor);
    }

    @Override
    public void set(double value) {
        next = (float) value;

        postChange(DEFAULT_EXECUTOR);
    }

    @Override
    public void set(double value, Executor notificationExecutor) {
        next = (float) value;

        postChange(notificationExecutor);
    }

    private void postChange(Executor executor) {
        dirty = true;

        if (notifying) return;

        executor.execute(this);
    }

    @Override
    public void run() {
        if (notifying) return;

        try {
            notifying = true;

            while (dirty) {
                current = next;
                dirty = false;

                notifyListeners(this, this);
            }
        } finally {
            notifying = false;
        }
    }

    @Override
    public void pull(@NotNull Listener<? super FloatVariable> listener) {
        listener.receive(this, this);
    }
}
