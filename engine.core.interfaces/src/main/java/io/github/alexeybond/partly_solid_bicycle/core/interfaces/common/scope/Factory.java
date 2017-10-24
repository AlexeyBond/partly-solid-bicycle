package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.ScopeMemberFactoryException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Factory that creates members for {@link Scope scopes}.
 *
 * @param <T> type of member
 * @param <A> type of factory argument
 */
public interface Factory<T, A> {
    /**
     * Create the member.
     *
     * @param arg factory argument
     * @return created member
     * @throws ScopeMemberFactoryException if exception occurs creating member
     * @throws NullPointerException if {@code arg} is {@code null} and this factory requires it not
     *                              to be {@code null}
     */
    @NotNull
    T create(@Nullable A arg)
            throws ScopeMemberFactoryException;
}
