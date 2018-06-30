package io.github.alexeybond.partly_solid_bicycle.core.impl.event.listeners;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Channel;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;
import org.jetbrains.annotations.NotNull;

public class ForwardingSubscription<T> extends UniqueSubscription<T> {
    @NotNull
    private final Channel<? super T> channel;

    public ForwardingSubscription(@NotNull Channel<? super T> channel) {
        this.channel = channel;
    }

    @Override
    protected void doReceive(@NotNull T event, @NotNull Topic<? extends T> topic) {
        channel.send(event);
    }
}
