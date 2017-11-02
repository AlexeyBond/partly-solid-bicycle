package io.github.alexeybond.partly_solid_bicycle.core.impl.scope;

import io.github.alexeybond.partly_solid_bicycle.core.impl.common.id.DefaultIdSet;
import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lazy.LazyMemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lazy.LazyReferenceProvider;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Factory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.ScopeOwner;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.InvalidScopeMemberReferenceStateException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.ScopeMemberFactoryException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class RelativeReferenceTest {
    private class ScopeOwnerImpl<T extends Scope> implements ScopeOwner<T> {
        private final T scope;

        private ScopeOwnerImpl(T scope) {
            this.scope = scope;
        }

        @Override
        public T getScope() {
            return scope;
        }
    }

    private class IdentityFactory<T> implements Factory<T, T> {
        @NotNull
        @Override
        public T create(@Nullable T arg) throws ScopeMemberFactoryException {
            assertNotNull(arg);
            return arg;
        }
    }

    private ScopeOwnerImpl<Scope<Object>> start;
    private final Object value = new Object();

    @Before public void setUp() {
        IdSet<Object> idSet = new DefaultIdSet<Object>();
        Scope<Object> nullS = new NullScope<Object>(idSet);
        start = new ScopeOwnerImpl<Scope<Object>>(
                new DefaultScope<Object, LazyMemberReference<Object>>(
                        new LazyReferenceProvider<Object>(), nullS));
        ScopeOwnerImpl<Scope<Object>> step1 = new ScopeOwnerImpl<Scope<Object>>(
                new DefaultScope<Object, LazyMemberReference<Object>>(
                        new LazyReferenceProvider<Object>(), nullS));
        ScopeOwnerImpl<Scope<Object>> step2 = new ScopeOwnerImpl<Scope<Object>>(
                new DefaultScope<Object, LazyMemberReference<Object>>(
                        new LazyReferenceProvider<Object>(), nullS));
        ScopeOwnerImpl<Scope<Object>> step3 = new ScopeOwnerImpl<Scope<Object>>(
                new DefaultScope<Object, LazyMemberReference<Object>>(
                        new LazyReferenceProvider<Object>(), nullS));
        start.scope.put(idSet.get("foo"), new IdentityFactory<Object>(), step1);
        step1.scope.put(idSet.get("bar"), new IdentityFactory<Object>(), step2);
        step2.scope.put(idSet.get("baz"), new IdentityFactory<Object>(), step3);
        step3.scope.put(idSet.get("buz"), new IdentityFactory<Object>(), value);
    }

    @Test public void Should_accessValueByPath() throws Exception {
        Object val = new RelativeReference<Object>("foo/bar/baz/buz", start).get();
        assertSame(value, val);
    }

    @Test(expected = InvalidScopeMemberReferenceStateException.class)
    public void Should_throwWhenInvalidPathGiven() throws Exception {
        new RelativeReference<Object>("foo/buzz", start).get();
    }

    @Test(expected = InvalidScopeMemberReferenceStateException.class)
    public void Should_throwWhenTryingToAccessValueAsScopeOwner() throws Exception {
        new RelativeReference<Object>("foo/bar/baz/buz/x", start).get();
    }
}
