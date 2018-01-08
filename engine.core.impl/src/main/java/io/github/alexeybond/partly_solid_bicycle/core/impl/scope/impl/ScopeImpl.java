package io.github.alexeybond.partly_solid_bicycle.core.impl.scope.impl;

import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lifecycle.reference.NullMemberReferenceLifecycle;
import io.github.alexeybond.partly_solid_bicycle.core.impl.util.ExceptionAccumulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Factory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.MemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.ScopeOwner;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.ScopeMemberFactoryException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.ScopeMemberNotFoundException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.UnsupportedScopeOperationException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.lifecycle.MemberReferenceLifecycle;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.visitor.ScopeVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ScopeImpl<
        T,
        TRef extends MemberReference<T>,
        TOwner extends ScopeOwner<Scope<T, TOwner>>
        > implements Scope<T, TOwner> {
    private static final Map EMPTY_MAP = new HashMap();

    @NotNull
    private MemberReferenceLifecycle<T, TRef> referenceLifecycle;

    @NotNull
    private final IdSet<T> idSet;

    @NotNull
    private final TOwner owner;

    @NotNull
    private final Scope<T, ?> superScope;

    @NotNull
    private Map<Id<T>, TRef> map;

    public ScopeImpl(
            @NotNull MemberReferenceLifecycle<T, TRef> referenceLifecycle,
            @NotNull IdSet<T> idSet,
            @NotNull TOwner owner,
            @NotNull Scope<T, ?> superScope,
            @NotNull Map<Id<T>, TRef> map) {
        this.referenceLifecycle = referenceLifecycle;
        this.idSet = idSet;
        this.owner = owner;
        this.superScope = superScope;
        this.map = map;
    }

    @NotNull
    @SuppressWarnings({"unchecked"})
    private <TT extends T> MemberReference<TT> uncheckedReturn(@NotNull TRef ref) {
        return (MemberReference<TT>) ref;
    }

    @NotNull
    @Override
    public IdSet<T> getIdSet() {
        return idSet;
    }

    @NotNull
    @Override
    public TOwner getOwner() {
        return owner;
    }

    @NotNull
    @Override
    public <TT extends T> MemberReference<TT> get(@NotNull Id<T> id)
            throws ScopeMemberNotFoundException {
        Map<Id<T>, TRef> map = this.map;

        TRef ref = map.get(id);

        if (null == ref) {
            ref = referenceLifecycle.forwardReference(getSuper(id));
            map.put(id, ref);
        }

        return uncheckedReturn(ref);
    }

    @NotNull
    @Override
    public <TT extends T> MemberReference<TT> getSuper(@NotNull Id<T> id)
            throws ScopeMemberNotFoundException {
        return superScope.get(id);
    }

    @Override
    public void removeId(@NotNull Id<T> id)
            throws UnsupportedScopeOperationException {
        Map<Id<T>, TRef> map = this.map;

        TRef ref = map.get(id);

        if (null == ref) return;

        boolean remove = true;

        try {
            referenceLifecycle.removeReference(id, ref);
        } catch (UnsupportedScopeOperationException e) {
            remove = false;
            throw e;
        } finally {
            if (remove) map.remove(id);
        }
    }

    @NotNull
    @Override
    public <TT extends T, A> MemberReference<TT> put(
            @NotNull Id<T> id, @NotNull Factory<TT, A> factory,
            @Nullable A arg)
            throws ScopeMemberFactoryException, UnsupportedScopeOperationException {
        Map<Id<T>, TRef> map = this.map;

        TRef ref = map.get(id);

        if (null == ref) {
            ref = referenceLifecycle.createReference(id, factory, arg);
        } else {
            TRef ref0 = ref;
            ref = referenceLifecycle.replaceReference(id, ref, factory, arg);
            if (ref != ref0) map.put(id, ref);
        }

        return uncheckedReturn(ref);
    }

    @Override
    public void accept(@NotNull ScopeVisitor<T, TOwner> visitor) {
        visitor.visitSuperScope(superScope);

        for (Map.Entry<Id<T>, TRef> entry : map.entrySet()) {
            visitor.visitReference(entry.getKey(), entry.getValue());
        }
    }

    public void clear(boolean dispose) {
        MemberReferenceLifecycle<T, TRef> originalLifecycle = this.referenceLifecycle;
        this.referenceLifecycle = NullMemberReferenceLifecycle.INSTANCE;

        Map<Id<T>, TRef> originalMap = this.map;
        this.map = (Map<Id<T>, TRef>) EMPTY_MAP;

        try {
            Throwable acc = ExceptionAccumulator.init();

            for (Map.Entry<Id<T>, TRef> entry : originalMap.entrySet()) {
                try {
                    if (dispose) {
                        originalLifecycle.disposeReference(entry.getKey(), entry.getValue());
                    } else {
                        originalLifecycle.removeReference(entry.getKey(), entry.getValue());
                    }
                } catch (Exception e) {
                    acc = ExceptionAccumulator.add(acc, e);
                }
            }

            ExceptionAccumulator.<RuntimeException>flush(acc);
        } finally {
            originalMap.clear();

            this.map = originalMap;
            this.referenceLifecycle = originalLifecycle;
        }
    }
}
