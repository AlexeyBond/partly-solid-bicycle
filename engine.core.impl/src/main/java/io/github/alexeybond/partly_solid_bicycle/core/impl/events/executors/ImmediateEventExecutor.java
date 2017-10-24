package io.github.alexeybond.partly_solid_bicycle.core.impl.events.executors;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventExecutor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventRunnable;

public enum  ImmediateEventExecutor implements EventExecutor {
    INSTANCE;

    @Override
    public void execute(EventRunnable eventRunnable) {
        eventRunnable.processEvent();
    }
}
