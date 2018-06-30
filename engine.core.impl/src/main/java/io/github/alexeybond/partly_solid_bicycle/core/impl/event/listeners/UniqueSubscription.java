package io.github.alexeybond.partly_solid_bicycle.core.impl.event.listeners;

import io.github.alexeybond.partly_solid_bicycle.core.impl.event.topic.NullTopic;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;
import org.jetbrains.annotations.NotNull;

public abstract class UniqueSubscription<T> implements Listener<T> {
    @NotNull
    private Topic<T> topic = NullTopic.get();

    private Object token;

    public void subscribe(@NotNull Topic<T> newTopic) {
        try {
            topic.unsubscribe(token, this);
        } catch (RuntimeException e) {
            topic = NullTopic.get();
            token = null;
            throw e;
        }

        try {
            token = newTopic.subscribe(this);
            topic = newTopic;
        } catch (RuntimeException e) {
            topic = NullTopic.get();
            token = null;
            throw e;
        }
    }

    public void clear() {
        if (NullTopic.INSTANCE != topic) {
            subscribe(NullTopic.<T>get());
        }
    }

    @Override
    public final void receive(@NotNull T event, @NotNull Topic<? extends T> topic) {
        doReceive(event, topic);
    }

    protected abstract void doReceive(@NotNull T event, @NotNull Topic<? extends T> topic);
}
