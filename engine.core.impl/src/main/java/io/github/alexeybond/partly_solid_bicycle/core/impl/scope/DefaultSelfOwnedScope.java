package io.github.alexeybond.partly_solid_bicycle.core.impl.scope;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.MemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.ScopeOwner;
import org.jetbrains.annotations.NotNull;

public class DefaultSelfOwnedScope<T, TRef extends MemberReference<T>>
        extends DefaultScope<T, TRef, DefaultSelfOwnedScope<T, TRef>>
        implements ScopeOwner<Scope<T, DefaultSelfOwnedScope<T, TRef>>> {
    public DefaultSelfOwnedScope(
            @NotNull ReferenceProvider<T, TRef> referenceProvider,
            @NotNull Scope<T, ?> superScope) {
        super(referenceProvider, superScope);
    }

    @NotNull
    @Override
    public Scope<T, DefaultSelfOwnedScope<T, TRef>> getScope() {
        return this;
    }

    @NotNull
    @Override
    public DefaultSelfOwnedScope<T, TRef> getOwner() {
        return this;
    }
}
