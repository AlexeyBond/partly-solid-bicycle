package io.github.alexeybond.partly_solid_bicycle.core.common.scope.impl;

import io.github.alexeybond.partly_solid_bicycle.core.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.common.scope.MemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.common.scope.ScopeVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.common.scope.exceptions.ScopeMemberNotFoundException;
import org.jetbrains.annotations.NotNull;

public class NullScope<T> implements Scope<T> {
    @NotNull
    private <TT extends T> MemberReference<TT> get0(@NotNull Id<T> id)
            throws ScopeMemberNotFoundException {
        throw new ScopeMemberNotFoundException(id);
    }

    @NotNull
    @Override
    public <TT extends T> MemberReference<TT> get(@NotNull Id<T> id)
            throws ScopeMemberNotFoundException {
        return get0(id);
    }

    @NotNull
    @Override
    public <TT extends T> MemberReference<TT> getOwn(@NotNull Id<T> id)
            throws ScopeMemberNotFoundException {
        return get0(id);
    }

    @NotNull
    @Override
    public <TT extends T> MemberReference<TT> getSuper(@NotNull Id<T> id)
            throws ScopeMemberNotFoundException {
        return get0(id);
    }

    @Override
    public void accept(@NotNull ScopeVisitor<T> visitor) {
        // no members
    }
}
