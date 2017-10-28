package io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lazy;

import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.ReferenceProvider;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Factory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.MemberReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LazyReferenceProvider<T>
        implements ReferenceProvider<T, LazyMemberReference<T>> {
    @Override
    public <A> LazyMemberReference<T> makeReference(@NotNull Factory<? extends T, A> factory, @Nullable A arg) {
        return new LazyMemberReference<T>(factory, arg);
    }

    @Override
    public LazyMemberReference<T> forwardReference(@NotNull MemberReference<? extends T> ref) {
        return new LazyMemberReference<T>(ref);
    }

    @Override
    public <A> LazyMemberReference<T> replaceReference(
            @NotNull LazyMemberReference<T> exist,
            @NotNull Factory<? extends T, A> factory,
            @Nullable A arg) {
        return exist.replace(factory, arg);
    }

    @Override
    public void removeReference(@NotNull LazyMemberReference<T> reference) {
        reference.removed();
    }
}
