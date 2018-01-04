package io.github.alexeybond.partly_solid_bicycle.core.interfaces.app;

import java.util.Collection;

public interface Module {
    void init(Collection<Object> env);

    void shutdown();
}
