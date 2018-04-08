package io.github.alexeybond.partly_solid_bicycle.core.impl.events.sources;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventListener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventSource;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.exceptions.NoEventOriginException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultProxyEventSource<T extends EventSource<? extends T>>
        extends DefaultEventSource<T>
        implements EventListener<T> {
    @NotNull
    private final T real;

    protected DefaultProxyEventSource(int capacity, @NotNull T real) {
        super(capacity);
        this.real = real;
    }

    @NotNull
    @Override
    public T origin() throws NoEventOriginException {
        return real.origin();
    }

    @Override
    public void onEvent(@NotNull T source, @Nullable Object initializer) {
        trigger(source, initializer);
    }
}
