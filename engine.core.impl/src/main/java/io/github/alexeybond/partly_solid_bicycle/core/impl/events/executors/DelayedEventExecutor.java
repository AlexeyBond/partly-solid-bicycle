package io.github.alexeybond.partly_solid_bicycle.core.impl.events.executors;

import com.badlogic.gdx.utils.Queue;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventExecutor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventRunnable;

/**
 * Non-thread safe delayed event executor.
 */
public abstract class DelayedEventExecutor implements EventExecutor {
    private final Queue<EventRunnable> queue;

    public DelayedEventExecutor(Queue<EventRunnable> queue) {
        this.queue = queue;
    }

    public DelayedEventExecutor() {
        this(new Queue<EventRunnable>());
    }

    @Override
    public void execute(EventRunnable eventRunnable) {
        queue.addLast(eventRunnable);
    }

    public void executeAll() {
        while (queue.size != 0) {
            queue.removeFirst().processEvent();
        }
    }
}
