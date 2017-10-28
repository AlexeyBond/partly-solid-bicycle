package io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lazy;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Factory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.MemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.InvalidScopeMemberReference;
import org.jetbrains.annotations.NotNull;

class LazyMemberReference<T> implements MemberReference<T> {
    LazyMemberReference(Factory<? extends T, ?> factory, Object factoryArg) {
        this.factory = factory;
        this.factoryArg = factoryArg;
        this.state = LazyReferenceState.PRE_INIT;
    }

    LazyMemberReference(MemberReference<? extends T> reference) {
        this.forwarding = reference;
        this.state = LazyReferenceState.FORWARDING;
    }

    Factory factory;
    Object factoryArg;
    T object;
    MemberReference<? extends T> forwarding;

    @NotNull
    LazyReferenceState state;

    @NotNull
    @Override
    public T get() throws InvalidScopeMemberReference {
        return state.resolve(this);
    }

    <A> LazyMemberReference<T> replace(Factory<? extends T, A> factory, A arg) {
        return state.replace(this, factory, arg);
    }

    void removed() {
        state.referenceRemoved(this);
    };
}
