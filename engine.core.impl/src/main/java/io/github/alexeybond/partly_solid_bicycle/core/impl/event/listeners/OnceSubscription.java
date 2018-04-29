package io.github.alexeybond.partly_solid_bicycle.core.impl.event.listeners;

import io.github.alexeybond.partly_solid_bicycle.core.impl.event.topic.NullTopic;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;
import org.jetbrains.annotations.NotNull;

public abstract class OnceSubscription<T> implements Listener<T> {
    @NotNull
    private Topic<T> topic;

    private Object token;

    public OnceSubscription(@NotNull Topic<T> topic) {
        this.topic = topic;
        this.token = topic.subscribe(this);
    }

    @Override
    public final void receive(@NotNull T event, @NotNull Topic<? extends T> topic) {
        try {
            doReceive(event, topic);
        } finally {
            try {
                this.topic.unsubscribe(token, this);
            } finally {
                this.topic = NullTopic.get();
                this.token = null;
            }
        }
    }

    protected abstract void doReceive(@NotNull T event, @NotNull Topic<? extends T> topic);
}
