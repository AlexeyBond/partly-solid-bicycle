package io.github.alexeybond.partly_solid_bicycle.core.events.executors;

import io.github.alexeybond.partly_solid_bicycle.core.events.EventExecutor;

public enum  ImmediateEventExecutor implements EventExecutor {
    INSTANCE;

    @Override
    public void execute(Runnable eventRunnable) {
        eventRunnable.run();
    }
}
