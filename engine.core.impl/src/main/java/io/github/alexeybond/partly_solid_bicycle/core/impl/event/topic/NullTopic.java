package io.github.alexeybond.partly_solid_bicycle.core.impl.event.topic;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;
import org.jetbrains.annotations.NotNull;

public enum NullTopic implements Topic {
    INSTANCE;

    @Override
    public Object subscribe(@NotNull Listener listener) {
        return null;
    }

    @Override
    public void unsubscribe(Object token, @NotNull Listener listener) {

    }

    @SuppressWarnings({"unchecked"})
    public static <T> Topic<T> get() {
        return (Topic<T>) INSTANCE;
    }
}
