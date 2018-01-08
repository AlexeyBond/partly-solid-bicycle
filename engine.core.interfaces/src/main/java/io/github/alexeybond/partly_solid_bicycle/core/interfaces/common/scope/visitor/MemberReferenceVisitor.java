package io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.visitor;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Factory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.MemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.visitor.Visitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MemberReferenceVisitor<T>
        extends Visitor<MemberReference<T>> {
    void visitResolvedMember(@NotNull T member);

    <A> void visitUnresolvedMember(@NotNull Factory<T, A> factory, @Nullable A arg);

    void visitForwardedReference(@NotNull MemberReference<? extends T> reference);
}
