package io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lifecycle.reference;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Factory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.MemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.lifecycle.MemberReferenceLifecycle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum NullMemberReferenceLifecycle implements MemberReferenceLifecycle {
    INSTANCE;

    private <T> T fail() {
        throw new IllegalStateException("Scope is (being) destroyed.");
    }

    @NotNull
    @Override
    public MemberReference createReference(@NotNull Id id, @NotNull Factory factory, @Nullable Object arg) {
        return fail();
    }

    @NotNull
    @Override
    public MemberReference forwardReference(@NotNull MemberReference reference) {
        return fail();
    }

    @Override
    public void removeReference(@NotNull Id id, @NotNull MemberReference reference) {
        fail();
    }

    @Override
    public void disposeReference(@NotNull Id id, @NotNull MemberReference reference) {
        fail();
    }

    @NotNull
    @Override
    public MemberReference replaceReference(
            @NotNull Id id,
            @NotNull MemberReference reference,
            @NotNull Factory factory, @Nullable Object arg) {
        return fail();
    }
}
