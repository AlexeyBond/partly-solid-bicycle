package io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.executors;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventExecutor;

public abstract class DelayedEventExecutor implements EventExecutor {
    protected abstract void put(Runnable runnable);

    protected abstract Runnable poll();

    @Override
    public void execute(Runnable eventRunnable) {
        put(eventRunnable);
    }

    public void executeAll() {
        Runnable r;

        while (null != (r = poll())) {
            r.run();
        }
    }
}
