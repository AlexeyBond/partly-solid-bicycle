package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GenericFactory<TR, TA> {
    @NotNull
    TR create(@Nullable TA arg);
}
