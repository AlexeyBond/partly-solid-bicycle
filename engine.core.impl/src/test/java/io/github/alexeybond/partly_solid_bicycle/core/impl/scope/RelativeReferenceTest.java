package io.github.alexeybond.partly_solid_bicycle.core.impl.scope;

import io.github.alexeybond.partly_solid_bicycle.core.impl.common.id.DefaultIdSet;
import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lazy.LazyMemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lazy.LazyReferenceProvider;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.InvalidScopeMemberReferenceStateException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class RelativeReferenceTest {
    private class ScopeOwnerImpl<T> extends DefaultSelfOwnedScope<T, LazyMemberReference<T>> {
        ScopeOwnerImpl(Scope<T, ?> parent) {
            super(new LazyReferenceProvider<T>(), parent);
        }
    }

    private ScopeOwnerImpl<Object> start;
    private final Object value = new Object();

    @Before public void setUp() {
        IdSet<Object> idSet = new DefaultIdSet<Object>();
        Scope<Object, ?> nullS = new NullScope<Object>(idSet);
        start = new ScopeOwnerImpl<Object>(nullS);
        ScopeOwnerImpl<Object> step1 = new ScopeOwnerImpl<Object>(nullS);
        ScopeOwnerImpl<Object> step2 = new ScopeOwnerImpl<Object>(nullS);
        ScopeOwnerImpl<Object> step3 = new ScopeOwnerImpl<Object>(nullS);
        start.getScope().put(idSet.get("foo"), IdentityMemberFactory.get(), step1);
        step1.getScope().put(idSet.get("bar"), IdentityMemberFactory.get(), step2);
        step2.getScope().put(idSet.get("baz"), IdentityMemberFactory.get(), step3);
        step3.getScope().put(idSet.get("buz"), IdentityMemberFactory.get(), value);
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
