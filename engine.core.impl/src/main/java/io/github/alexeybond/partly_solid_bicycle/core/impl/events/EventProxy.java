package io.github.alexeybond.partly_solid_bicycle.core.impl.events;

import io.github.alexeybond.partly_solid_bicycle.core.impl.events.listeners.Subscription;
import io.github.alexeybond.partly_solid_bicycle.core.impl.events.sources.DefaultEventSource;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventSource;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.exceptions.NoEventOriginException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventProxy<T extends EventSource<T>> extends DefaultEventSource<T> {
    private final Subscription<T> subscription = new Subscription<T>() {
        @Override
        public void onEvent(@NotNull T source, @Nullable Object initializer) {
            EventProxy.this.trigger(source, initializer);
        }
    };

    private final T origin;

    public EventProxy(EventSource<T> source) {
        super(0);
        origin = source.origin();
    }

    @NotNull
    @Override
    public T origin() throws NoEventOriginException {
        return origin;
    }
}
