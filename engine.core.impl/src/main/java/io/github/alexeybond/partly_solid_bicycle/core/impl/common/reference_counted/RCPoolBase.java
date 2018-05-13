package io.github.alexeybond.partly_solid_bicycle.core.impl.common.reference_counted;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.reference_counted.RCPool;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.reference_counted.ReferenceCounted;
import org.jetbrains.annotations.NotNull;

public abstract class RCPoolBase<T extends ReferenceCounted> implements RCPool<T> {
    abstract void returnItem(@NotNull T item);
}
