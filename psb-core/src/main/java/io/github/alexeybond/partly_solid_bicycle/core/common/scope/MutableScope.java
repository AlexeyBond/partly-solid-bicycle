package io.github.alexeybond.partly_solid_bicycle.core.common.scope;

import io.github.alexeybond.partly_solid_bicycle.core.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.common.scope.exceptions.ScopeMemberFactoryException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * {@link Scope} that allows to add members.
 *
 * @param <T> scope member type
 */
public interface MutableScope<T> extends Scope<T> {
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
     */
    @NotNull
    <TT extends T, A> MemberReference<TT> put(@NotNull Id<T> id, @NotNull Factory<TT, A> factory, @Nullable A arg)
            throws ScopeMemberFactoryException;
}
