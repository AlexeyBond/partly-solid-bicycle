package io.github.alexeybond.partly_solid_bicycle.core.impl.common.reference_counted;

import com.badlogic.gdx.utils.Queue;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory.GenericFactory;
import org.jetbrains.annotations.NotNull;

public class RCPoolImpl<T extends RCPooledBase<T>, TA> extends RCPoolBase<T> {
    @NotNull
    private final Queue<T> queue;

    @NotNull
    private final GenericFactory<T, TA> factory;

    private final int maxCapacity;

    private TA argument;

    public RCPoolImpl(
            @NotNull GenericFactory<T, TA> factory,
            TA argument,
            int preAlloc,
            int maxCapacity) {
        this.factory = factory;
        this.argument = argument;
        this.maxCapacity = maxCapacity;

        queue = new Queue<T>(preAlloc);

        for (int i = 0; i < preAlloc; i++) {
            queue.addLast(factory.create(argument));
        }
    }

    @Override
    void returnItem(@NotNull T item) {
        if (queue.size < maxCapacity) {
            queue.addLast(item);
        }
    }

    @Override
    public T acquire() {
        T item;
        if (queue.size != 0) {
            item = queue.removeFirst();
            item.resetRC();
        } else {
            item = factory.create(argument);
        }
        item.acquire();
        return item;
    }
}
