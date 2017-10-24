package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.ScopeMemberNotFoundException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.visitor.Visitable;
import org.jetbrains.annotations.NotNull;

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
public interface Scope<T> extends Visitable<ScopeVisitor<T>> {
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
}
