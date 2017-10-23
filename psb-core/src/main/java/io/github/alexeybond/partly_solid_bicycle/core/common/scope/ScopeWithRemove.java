package io.github.alexeybond.partly_solid_bicycle.core.common.scope;

import io.github.alexeybond.partly_solid_bicycle.core.common.id.Id;
import org.jetbrains.annotations.NotNull;

public interface ScopeWithRemove<T> extends Scope<T> {
    void removeId(@NotNull Id<T> id);

    void removeMember(@NotNull Id<T> id);
}
