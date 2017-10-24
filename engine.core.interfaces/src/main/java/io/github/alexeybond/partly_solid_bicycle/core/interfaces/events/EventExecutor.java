package io.github.alexeybond.partly_solid_bicycle.core.interfaces.events;

public interface EventExecutor {
    void execute(Runnable eventRunnable);
}
