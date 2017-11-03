package io.github.alexeybond.partly_solid_bicycle.core.impl.scope;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.*;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.ScopeMemberFactoryException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.ScopeMemberNotFoundException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.UnsupportedScopeOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Self-owned scope with no members.
 *
 * @param <T>
 */
public class NullScope<T>
        implements Scope<T, NullScope<T>>, ScopeOwner<Scope<T, NullScope<T>>> {
    @NotNull
    private final IdSet<T> idSet;

    public NullScope(@NotNull IdSet<T> idSet) {
        this.idSet = idSet;
    }

    @NotNull
    private <TT extends T> MemberReference<TT> get0(@NotNull Id<T> id)
            throws ScopeMemberNotFoundException {
        throw new ScopeMemberNotFoundException(id);
    }

    @NotNull
    @Override
    public IdSet<T> getIdSet() {
        return idSet;
    }

    @NotNull
    @Override
    public NullScope<T> getOwner() {
        return this;
    }

    @NotNull
    @Override
    public NullScope<T> getScope() {
        return this;
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
    public void removeId(@NotNull Id<T> id)
            throws UnsupportedScopeOperationException {
        // there will be no element with that id when method returns. OK
    }

    @NotNull
    @Override
    public <TT extends T, A> MemberReference<TT> put(
        @NotNull Id<T> id,
        @NotNull Factory<TT, A> factory,
        @Nullable A arg)
            throws ScopeMemberFactoryException, UnsupportedScopeOperationException {
        throw new UnsupportedScopeOperationException("#put() not supported in NullScope.");
    }

    @Override
    public void accept(@NotNull ScopeVisitor<T, Scope<T, NullScope<T>>> visitor) {
        // no members
    }
}
