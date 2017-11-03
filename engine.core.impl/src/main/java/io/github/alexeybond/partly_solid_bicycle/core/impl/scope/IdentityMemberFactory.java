package io.github.alexeybond.partly_solid_bicycle.core.impl.scope;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Factory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.ScopeMemberFactoryException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * {@link Factory Scope member factory} that just returns a member instance passed as argument.
 *
 * @param <T>
 */
public class IdentityMemberFactory<T> implements Factory<T, T> {
    @NotNull
    @Override
    public T create(@Nullable T arg)
            throws ScopeMemberFactoryException {
        if (null == arg) throw new ScopeMemberFactoryException("Argument should not be null");
        return arg;
    }

    private static final IdentityMemberFactory INSTANCE = new IdentityMemberFactory();

    @SuppressWarnings({"unchecked"})
    public static <T> IdentityMemberFactory<T> get() {
        return (IdentityMemberFactory<T>) INSTANCE;
    }
}
