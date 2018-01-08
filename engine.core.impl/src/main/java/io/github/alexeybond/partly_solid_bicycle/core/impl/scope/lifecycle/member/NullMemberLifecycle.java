package io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lifecycle.member;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Factory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.lifecycle.IndividualMemberLifecycle;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.lifecycle.MemberLifecycle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum NullMemberLifecycle implements MemberLifecycle, IndividualMemberLifecycle {
    INSTANCE;

    @NotNull
    @Override
    public IndividualMemberLifecycle onResolved(
            @NotNull Id id, @NotNull Object member, @NotNull Factory factory, @Nullable Object arg) {
        return this;
    }

    @NotNull
    @Override
    public IndividualMemberLifecycle onForwarded(@NotNull Id id, @NotNull Object member) {
        return this;
    }

    @Override
    public void onRemoved(@NotNull Id id, @NotNull Object member) {

    }

    @Override
    public void onDisposed(@NotNull Id id, @NotNull Object member) {

    }

    @Nullable
    @Override
    public Object forward(@NotNull Object member, @NotNull Scope scope) {
        return null;
    }
}
