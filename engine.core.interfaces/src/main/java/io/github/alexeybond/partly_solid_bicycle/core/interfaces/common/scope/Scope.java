package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.ScopeMemberFactoryException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.ScopeMemberNotFoundException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.UnsupportedScopeOperationException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.visitor.Visitable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Scope is a hierarchical key-value storage of items (scope members) of some type.
 *
 * <p>
 *  Scopes are hierarchical i.e. a scope may have a parent/super scope/scopes. Any member available in super-scope
 *  should available in the sub-scope unless it is hidden by other member with the same identifier.
 * </p>
 *
 * @param <T> type of scope member
 */
public interface Scope<T, TOwner extends ScopeOwner<Scope<T, TOwner>>>
        extends Visitable<ScopeVisitor<T, Scope<T, TOwner>>> {
    /**
     * @return {@link IdSet} that provides identifiers for objects within this scope
     */
    @NotNull
    IdSet<T> getIdSet();

    /**
     * @return {@link ScopeOwner owner} this scope belongs to
     */
    @NotNull
    TOwner getOwner();

    /**
     * Get reference to member with given identifier.
     *
     * @param id   member id
     * @param <TT> member type
     * @return reference to scope member
     * @throws ScopeMemberNotFoundException if there is no member with given identifier
     */
    @NotNull
    <TT extends T> MemberReference<TT> get(@NotNull Id<T> id)
        throws ScopeMemberNotFoundException;

    /**
     * Get own member with given id.
     *
     * @param id   member id
     * @param <TT> member type
     * @return member reference
     * @throws ScopeMemberNotFoundException if there is no member with given id
     */
    @NotNull
    <TT extends T> MemberReference<TT> getOwn(@NotNull Id<T> id)
        throws ScopeMemberNotFoundException;

    /**
     * Get member of super-scope with given id.
     *
     * @param id   member id
     * @param <TT> member type
     * @return member reference
     * @throws ScopeMemberNotFoundException if there is no member with given id in any of super-scopes of this scope
     */
    @NotNull
    <TT extends T> MemberReference<TT> getSuper(@NotNull Id<T> id)
        throws ScopeMemberNotFoundException;

    /**
     * Remove member with given id. Has no effect if there is no member with given id.
     *
     * @param id member id
     * @throws UnsupportedScopeOperationException if this scope may contain items and does not support
     *                                            remove operation
     */
    void removeId(@NotNull Id<T> id)
            throws UnsupportedScopeOperationException;

    /**
     * Create new member with given id using given factory or return reference to an exist own member with
     * given identifier if any.
     *
     * <p>
     *  Member may be created lazily on first request to reference or immediately on {@code put} call depending
     *  on implementation and state of the scope.
     * </p>
     *
     * @param id      member id
     * @param factory factory that should create new member
     * @param arg     argument to pass to factory
     * @param <TT>    member type
     * @param <A>     factory argument type
     * @return reference to present or created member with given id
     * @throws ScopeMemberFactoryException if factory was called immediately and has thrown the exception
     * @throws UnsupportedScopeOperationException if this scope is read-only
     */
    @NotNull
    <TT extends T, A> MemberReference<TT> put(@NotNull Id<T> id, @NotNull Factory<TT, A> factory, @Nullable A arg)
            throws ScopeMemberFactoryException, UnsupportedScopeOperationException;
}
