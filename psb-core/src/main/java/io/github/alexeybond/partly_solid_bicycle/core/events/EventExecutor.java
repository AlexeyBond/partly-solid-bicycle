package io.github.alexeybond.partly_solid_bicycle.core.events;

public interface EventExecutor {
    void execute(Runnable eventRunnable);
}
