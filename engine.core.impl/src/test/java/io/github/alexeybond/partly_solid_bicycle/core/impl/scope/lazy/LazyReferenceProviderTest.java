package io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lazy;

import io.github.alexeybond.partly_solid_bicycle.core.impl.common.id.DefaultIdSet;
import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.DefaultScope;
import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.NullScope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Factory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.MemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.InvalidScopeMemberReferenceStateException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.ScopeMemberFactoryException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public class LazyReferenceProviderTest {
    @Test public void testLoopDetection() {
        final Scope<Object> scope = new DefaultScope<Object, LazyMemberReference<Object>>(
                new LazyReferenceProvider<Object>(), new NullScope<Object>(new DefaultIdSet<Object>()));
        scope.put(scope.getIdSet().get("A"), new Factory<Object, Object>() {
            @NotNull
            @Override
            public Object create(@Nullable Object arg) throws ScopeMemberFactoryException {
                return scope.get(scope.getIdSet().get("B")).get();
            }
        }, null);

        scope.put(scope.getIdSet().get("B"), new Factory<Object, Object>() {
            @NotNull
            @Override
            public Object create(@Nullable Object arg) throws ScopeMemberFactoryException {
                return scope.get(scope.getIdSet().get("A")).get();
            }
        }, null);

        MemberReference<Object> reference = scope.get(scope.getIdSet().get("A"));

        try {
            reference.get();
            fail();
        } catch (InvalidScopeMemberReferenceStateException expected) {
            assertNull(expected.getCause());
        }
    }

    @Test public void testLazyCreation() {
        Factory<Object, Object> factoryMock = mock(Factory.class);
        Object arg = new Object(), obj = new Object();

        when(factoryMock.create(same(arg))).thenReturn(obj).thenThrow(RuntimeException.class);

        final Scope<Object> scope = new DefaultScope<Object, LazyMemberReference<Object>>(
                new LazyReferenceProvider<Object>(), new NullScope<Object>(new DefaultIdSet<Object>()));

        scope.put(scope.getIdSet().get("A"), factoryMock, arg);

        verifyZeroInteractions(factoryMock);

        Object r1 = scope.get(scope.getIdSet().get("A")).get();
        Object r2 = scope.get(scope.getIdSet().get("A")).get();

        assertSame(obj, r1);
        assertSame(obj, r2);

        verify(factoryMock, times(1)).create(any());
    }
}
