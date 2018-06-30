package io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Variable;

import java.util.concurrent.Executor;

public interface FloatVariable extends Variable<FloatVariable> {
    float getFloat();

    double getDouble();

    void set(float value);

    void set(float value, Executor notificationExecutor);

    void set(double value);

    void set(double value, Executor notificationExecutor);
}
