package io.github.alexeybond.partly_solid_bicycle.core.impl.event.topic;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Notifier;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class ExecutorQueueTopic<T> extends QueueTopic<T> {
    @NotNull
    private final Executor executor;

    private boolean enqueued = false;

    public ExecutorQueueTopic(@NotNull Notifier<T> notifier, int initialCapacity, @NotNull Executor executor) {
        super(notifier, initialCapacity);
        this.executor = executor;
    }

    @Override
    public void send(@NotNull T event) {
        super.send(event);

        if (enqueued) return;

        executor.execute(this);
    }

    @Override
    public void run() {
        try {
            super.run();
        } finally {
            enqueued = false;
        }
    }
}
