package io.github.alexeybond.partly_solid_bicycle.core.impl.scope;

import com.badlogic.gdx.utils.IdentityMap;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Factory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.MemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.ScopeOwner;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.ScopeMemberFactoryException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.ScopeMemberNotFoundException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.visitor.ScopeVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DefaultScope<
    T,
    TRef extends MemberReference<T>,
    TOwner extends ScopeOwner<Scope<T, TOwner>>>
        implements Scope<T, TOwner> {
    @NotNull
    private final ReferenceProvider<T, TRef> referenceProvider;

    @NotNull
    private final IdentityMap<Id<T>, TRef> map;

    @NotNull
    private final Scope<T, ?> superScope;

    @NotNull
    private final IdSet<T> idSet;

    public DefaultScope(
            @NotNull ReferenceProvider<T, TRef> referenceProvider,
            @NotNull Scope<T, ?> superScope) {
        this.referenceProvider = referenceProvider;
        this.superScope = superScope;
        this.idSet = superScope.getIdSet();
        map = new IdentityMap<Id<T>, TRef>();
    }

    private @NotNull
    TRef getPresent0(@NotNull Id<T> id) {
        TRef reference = map.get(id);

        if (null == reference) {
            throw new ScopeMemberNotFoundException(id);
        }

        return reference;
    }

    private @NotNull
    <TT> MemberReference<TT> getAndForwardSuper(@NotNull Id<T> id) {
        MemberReference<? extends T> superRef = superScope.get(id);

        TRef fwd = referenceProvider.forwardReference(superRef);

        if (null != fwd) {
            map.put(id, fwd);

            @SuppressWarnings({"unchecked"})
            MemberReference<TT> uFwd = (MemberReference<TT>) fwd;

            return uFwd;
        } else {
            throw new ScopeMemberNotFoundException(id);
        }
    }

    @NotNull
    @Override
    public IdSet<T> getIdSet() {
        return idSet;
    }

    @NotNull
    @Override
    public abstract TOwner getOwner();

    @NotNull
    @Override
    public final <TT extends T> MemberReference<TT> get(@NotNull Id<T> id)
            throws ScopeMemberNotFoundException {
        try {
            return getPresent(id);
        } catch (ScopeMemberNotFoundException e) {
            return getAndForwardSuper(id);
        }
    }

    @NotNull
    private <TT extends T> MemberReference<TT> getPresent(@NotNull Id<T> id)
            throws ScopeMemberNotFoundException {
        @SuppressWarnings({"unchecked"})
        MemberReference<TT> uncheckedReference = (MemberReference<TT>) getPresent0(id);

        return uncheckedReference;
    }

    @NotNull
    @Override
    public final <TT extends T> MemberReference<TT> getSuper(@NotNull Id<T> id)
            throws ScopeMemberNotFoundException {
        return superScope.get(id);
    }

    @Override
    public void accept(@NotNull ScopeVisitor<T, TOwner> visitor) {
        visitor.visitSuperScope(superScope);

        for (IdentityMap.Entry<Id<T>, TRef> entry : map) {
            // IdentityMap's iterator does not throw ConcurrentModificationException when
            // entry is deleted during iteration.
            visitor.visitReference(entry.key, entry.value);
        }
    }

    @NotNull
    @Override
    public <TT extends T, A> MemberReference<TT> put(
            @NotNull Id<T> id,
            @NotNull Factory<TT, A> factory,
            @Nullable A arg)
            throws ScopeMemberFactoryException {
        TRef ref, origRef = null;

        try {
            ref = getPresent0(id);
            origRef = ref;

            ref = referenceProvider.replaceReference(ref, factory, arg);
        } catch (ScopeMemberNotFoundException e) {
            ref = referenceProvider.makeReference(factory, arg);
        }

        if (ref != origRef) {
            map.put(id, ref);
        }

        @SuppressWarnings({"unchecked"})
        MemberReference<TT> uRef = (MemberReference<TT>) ref;

        return uRef;
    }

    @Override
    public void removeId(@NotNull Id<T> id) {
        TRef ref = map.remove(id);

        referenceProvider.removeReference(ref);
    }

    protected void clear() {
        // TODO:: Implement
    }
}
