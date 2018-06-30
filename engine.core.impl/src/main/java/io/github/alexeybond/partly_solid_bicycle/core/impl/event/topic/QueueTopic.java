package io.github.alexeybond.partly_solid_bicycle.core.impl.event.topic;

import com.badlogic.gdx.utils.Queue;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Channel;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Notifier;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;
import org.jetbrains.annotations.NotNull;

public class QueueTopic<T>
        extends BaseNotifierTopic<T>
        implements Topic<T>, Channel<T>, Runnable {
    @NotNull
    private final Queue<T> queue;

    public QueueTopic(@NotNull Notifier<T> notifier, int initialCapacity) {
        super(notifier);
        this.queue = new Queue<T>(initialCapacity);
    }

    @Override
    public void send(@NotNull T event) {
        queue.addLast(event);
    }

    @Override
    public void run() {
        while (queue.size != 0) {
            T event = queue.removeFirst();

            notifier.notifyListeners(event, this);
        }
    }
}
