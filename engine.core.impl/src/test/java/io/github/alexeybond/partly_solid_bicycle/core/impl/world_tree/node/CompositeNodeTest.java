package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node;

import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver.NullChildResolver;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory.IdentityNodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.populator.NullPopulator;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.populator.PredefinedArgumentsPopulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ChildLogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodePopulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeVisitor;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public class CompositeNodeTest {
    private LogicNode parentMock;
    private Id[] idMocks;
    private ChildLogicNode[] childMocks;

    @Before
    public void setUp() {
        parentMock = mock(LogicNode.class);

        idMocks = new Id[8];
        childMocks = new ChildLogicNode[idMocks.length];

        for (int i = 0; i < idMocks.length; i++) {
            idMocks[i] = mock(Id.class);
            childMocks[i] = mock(ChildLogicNode.class);
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetComponentThrows() {
        ChildLogicNode node = new GroupNode(NullChildResolver.INSTANCE, NullPopulator.INSTANCE);
        node.onConnected(parentMock, idMocks[0]);
        node.getComponent();
    }

    @Test
    public void testCreateChildrenProvidedByPopulator() {
        NodePopulator populator = new PredefinedArgumentsPopulator<ChildLogicNode>(
                IdentityNodeFactory.INSTANCE,
                new HashMap<Id<LogicNode>, ChildLogicNode>() {{
                    put(idMocks[0], childMocks[0]);
                    put(idMocks[1], childMocks[1]);
                    put(idMocks[2], childMocks[2]);
                }}
        );

        ChildLogicNode node = new GroupNode(NullChildResolver.INSTANCE, populator);

        verifyZeroInteractions(childMocks[0], childMocks[1], childMocks[2]);

        node.onConnected(parentMock, idMocks[0]);

        verify(childMocks[0]).onConnected(same(node), same(idMocks[0]));
        verify(childMocks[1]).onConnected(same(node), same(idMocks[1]));
        verify(childMocks[2]).onConnected(same(node), same(idMocks[2]));

        assertSame(childMocks[0], node.get(idMocks[0]));
        assertSame(childMocks[1], node.get(idMocks[1]));
        assertSame(childMocks[2], node.get(idMocks[2]));
    }

    @Test
    public void testGetOrAdd() {
        ChildLogicNode node = new GroupNode(NullChildResolver.INSTANCE, NullPopulator.INSTANCE);
        node.onConnected(parentMock, idMocks[0]);

        LogicNode child = node.getOrAdd(idMocks[0], IdentityNodeFactory.INSTANCE, childMocks[0]);

        assertSame(childMocks[0], child);
        assertSame(childMocks[0], node.get(idMocks[0]));

        verify(childMocks[0]).onConnected(same(node), same(idMocks[0]));
        verifyNoMoreInteractions(childMocks[0]);

        child = node.getOrAdd(idMocks[0], IdentityNodeFactory.INSTANCE, childMocks[1]);

        assertSame(childMocks[0], child);
        assertSame(childMocks[0], node.get(idMocks[0]));

        verifyNoMoreInteractions(childMocks[0], childMocks[1]);
    }

    @Test
    public void testDisconnect() {
        ChildLogicNode node = new GroupNode(NullChildResolver.INSTANCE, NullPopulator.INSTANCE);
        node.onConnected(parentMock, idMocks[0]);

        node.getOrAdd(idMocks[0], IdentityNodeFactory.INSTANCE, childMocks[0]);
        node.getOrAdd(idMocks[1], IdentityNodeFactory.INSTANCE, childMocks[1]);
        node.getOrAdd(idMocks[2], IdentityNodeFactory.INSTANCE, childMocks[2]);

        verify(childMocks[0]).onConnected(same(node), same(idMocks[0]));
        verify(childMocks[1]).onConnected(same(node), same(idMocks[1]));
        verify(childMocks[2]).onConnected(same(node), same(idMocks[2]));

        verifyNoMoreInteractions(childMocks[0], childMocks[1], childMocks[2]);

        node.onDisconnected(parentMock);

        verify(childMocks[0]).onDisconnected(same(node));
        verify(childMocks[1]).onDisconnected(same(node));
        verify(childMocks[2]).onDisconnected(same(node));

        verifyNoMoreInteractions(childMocks[0], childMocks[1], childMocks[2]);
    }

    @Test
    public void testRemove() {
        ChildLogicNode node = new GroupNode(NullChildResolver.INSTANCE, NullPopulator.INSTANCE);
        node.onConnected(parentMock, idMocks[0]);

        node.getOrAdd(idMocks[0], IdentityNodeFactory.INSTANCE, childMocks[0]);
        node.getOrAdd(idMocks[1], IdentityNodeFactory.INSTANCE, childMocks[1]);
        node.getOrAdd(idMocks[2], IdentityNodeFactory.INSTANCE, childMocks[2]);

        verify(childMocks[0]).onConnected(same(node), same(idMocks[0]));
        verify(childMocks[1]).onConnected(same(node), same(idMocks[1]));
        verify(childMocks[2]).onConnected(same(node), same(idMocks[2]));

        verifyNoMoreInteractions(childMocks[0], childMocks[1], childMocks[2]);

        node.remove(idMocks[0]);
        node.remove(childMocks[1]);
        node.remove(idMocks[3]);

        try {
            node.get(idMocks[0]);
            fail();
        } catch (NoSuchElementException e) {/* OK */}

        try {
            node.get(idMocks[1]);
            fail();
        } catch (NoSuchElementException e) {/* OK */}

        assertSame(childMocks[2], node.get(idMocks[2]));
    }

    @Test
    public void testAcceptVisitor() {
        ChildLogicNode node = new GroupNode(NullChildResolver.INSTANCE, NullPopulator.INSTANCE);
        node.onConnected(parentMock, idMocks[0]);

        node.getOrAdd(idMocks[0], IdentityNodeFactory.INSTANCE, childMocks[0]);
        node.getOrAdd(idMocks[1], IdentityNodeFactory.INSTANCE, childMocks[1]);
        node.getOrAdd(idMocks[2], IdentityNodeFactory.INSTANCE, childMocks[2]);

        NodeVisitor visitor = mock(NodeVisitor.class);
        node.accept(visitor);

        verify(visitor).visitChild(same(idMocks[0]), same(childMocks[0]));
        verify(visitor).visitChild(same(idMocks[1]), same(childMocks[1]));
        verify(visitor).visitChild(same(idMocks[2]), same(childMocks[2]));

        verifyNoMoreInteractions(visitor);
    }
}
