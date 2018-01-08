package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.InvalidScopeMemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.InvalidScopeMemberReferenceStateException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.visitor.MemberReferenceVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.visitor.Visitable;
import org.jetbrains.annotations.NotNull;

/**
 * Reference to a member of scope.
 *
 * <p>
 *  Reference may refer to a member that is not exist as it was not created or as it was already removed.
 * </p>
 *
 * @param <T> scope member type
 */
public interface MemberReference<T> extends Visitable<MemberReferenceVisitor<T>> {
    /**
     * Get the scope member.
     *
     * @return the scope member
     * @throws InvalidScopeMemberReference (with cause exception) if reference tried to create a member
     *                                     lazily and member creation failed with exception
     * @throws InvalidScopeMemberReference if the member was removed from scope
     * @throws InvalidScopeMemberReferenceStateException if the member is temporally unavailable because of
     *                                                   reference state that may change in future
     */
    @NotNull
    T get()
            throws InvalidScopeMemberReference, InvalidScopeMemberReferenceStateException;
}
