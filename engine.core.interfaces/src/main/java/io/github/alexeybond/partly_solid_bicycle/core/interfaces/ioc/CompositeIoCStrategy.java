package io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc;

import org.jetbrains.annotations.NotNull;

public interface CompositeIoCStrategy extends IoCStrategy {
    void add(@NotNull Object key, @NotNull IoCStrategy strategy);
}
