package io.github.alexeybond.partly_solid_bicycle.core.impl.scope;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Factory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.MemberReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ReferenceProvider<T, TRef extends MemberReference<T>> {
    <A> TRef makeReference(@NotNull Factory<? extends T, A> factory, @Nullable A arg);

    TRef forwardReference(@NotNull MemberReference<? extends T> ref);

    <A> TRef replaceReference(@NotNull TRef exist, @NotNull Factory<? extends T, A> factory, @Nullable A arg);

    void removeReference(@NotNull TRef ref);
}
