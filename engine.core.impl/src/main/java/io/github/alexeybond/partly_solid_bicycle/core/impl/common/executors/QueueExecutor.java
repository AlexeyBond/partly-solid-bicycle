package io.github.alexeybond.partly_solid_bicycle.core.impl.common.executors;

import com.badlogic.gdx.utils.Queue;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class QueueExecutor implements Executor, Runnable {
    private final Queue<Runnable> queue;

    public QueueExecutor(int initialCapacity) {
        queue = new Queue<Runnable>(initialCapacity);
    }

    @Override
    public void execute(@NotNull Runnable command) {
        queue.addFirst(command);
    }

    @Override
    public void run() {
        while (queue.size > 0) {
            queue.removeLast().run();
        }
    }
}
