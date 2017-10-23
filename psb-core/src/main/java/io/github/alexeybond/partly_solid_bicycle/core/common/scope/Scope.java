package io.github.alexeybond.partly_solid_bicycle.core.common.scope;

import io.github.alexeybond.partly_solid_bicycle.core.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.common.visitor.Visitable;
import org.jetbrains.annotations.NotNull;

public interface Scope<T> extends Visitable<ScopeVisitor<T>> {
    @NotNull
    <TT extends T> MemberReference<TT> get(@NotNull Id<T> id);

    @NotNull
    <TT extends T, A> MemberReference<TT> put(@NotNull Id<T> id, @NotNull Factory<TT, A> factory, A arg);

    @NotNull
    <TT extends T> MemberReference<TT> getOwn(@NotNull Id<T> id);

    @NotNull
    <TT extends T> MemberReference<TT> getSuper(@NotNull Id<T> id);
}
