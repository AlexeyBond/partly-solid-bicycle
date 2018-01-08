package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.lifecycle;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Factory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.MemberReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MemberReferenceLifecycle<TMem, TRef extends MemberReference<TMem>> {
    @NotNull
    <A> TRef createReference(@NotNull Id<TMem> id, @NotNull Factory<? extends TMem, A> factory, @Nullable A arg);

    @NotNull
    TRef forwardReference(
            @NotNull MemberReference<? extends TMem> reference);

    @NotNull
    <A> TRef replaceReference(
            @NotNull Id<TMem> id, @NotNull TRef reference,
            @NotNull Factory<? extends TMem, A> factory, @Nullable A arg);

    void removeReference(
            @NotNull Id<TMem> id,
            @NotNull TRef reference);

    void disposeReference(
            @NotNull Id<TMem> id,
            @NotNull TRef reference);
}
