package io.github.alexeybond.partly_solid_bicycle.core.impl.event.topic;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Notifier;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;
import org.jetbrains.annotations.NotNull;

public class BaseNotifierTopic<T> implements Topic<T> {
    @NotNull
    protected final Notifier<T> notifier;

    public BaseNotifierTopic(@NotNull Notifier<T> notifier) {
        this.notifier = notifier;
    }

    @Override
    public Object subscribe(@NotNull Listener<? super T> listener) {
        return notifier.subscribe(listener);
    }

    @Override
    public void unsubscribe(Object token, @NotNull Listener<? super T> listener) {
        notifier.unsubscribe(token, listener);
    }
}
