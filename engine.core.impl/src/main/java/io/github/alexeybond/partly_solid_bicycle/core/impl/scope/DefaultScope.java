package io.github.alexeybond.partly_solid_bicycle.core.impl.scope;

import com.badlogic.gdx.utils.IdentityMap;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.*;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.ScopeMemberFactoryException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.ScopeMemberNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultScope<T, TRef extends MemberReference<T>>
        implements Scope<T> {
    @NotNull
    private final ReferenceProvider<T, TRef> referenceProvider;

    @NotNull
    private final IdentityMap<Id<T>, TRef> map;

    @NotNull
    private final Scope<T> superScope;

    @NotNull
    private final IdSet<T> idSet;

    public DefaultScope(
            @NotNull ReferenceProvider<T, TRef> referenceProvider,
            @NotNull Scope<T> superScope) {
        this.referenceProvider = referenceProvider;
        this.superScope = superScope;
        this.idSet = superScope.getIdSet();
        map = new IdentityMap<Id<T>, TRef>();
    }

    private @NotNull
    TRef getOwn0(@NotNull Id<T> id) {
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
    public final <TT extends T> MemberReference<TT> get(@NotNull Id<T> id)
            throws ScopeMemberNotFoundException {
        try {
            return getOwn(id);
        } catch (ScopeMemberNotFoundException e) {
            return getAndForwardSuper(id);
        }
    }

    @NotNull
    @Override
    public final <TT extends T> MemberReference<TT> getOwn(@NotNull Id<T> id)
            throws ScopeMemberNotFoundException {
        @SuppressWarnings({"unchecked"})
        MemberReference<TT> uncheckedReference = (MemberReference<TT>) getOwn0(id);

        return uncheckedReference;
    }

    @NotNull
    @Override
    public final <TT extends T> MemberReference<TT> getSuper(@NotNull Id<T> id)
            throws ScopeMemberNotFoundException {
        return superScope.get(id);
    }

    @Override
    public void accept(@NotNull ScopeVisitor<T> visitor) {
        for (IdentityMap.Entry<Id<T>, TRef> entry : map) {
            visitor.visitMember(entry.key, entry.value.get());
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
            ref = getOwn0(id);
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
}
