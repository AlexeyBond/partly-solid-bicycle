package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ComponentConnector;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeVisitor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public class ComponentNodeTest {
    private ComponentConnector<Object> connectorMock;
    private Object componentMock;
    private LogicNode parentMock;

    @Before
    public void setUp() {
        connectorMock = mock(ComponentConnector.class);
        componentMock = mock(Object.class);
        parentMock = mock(LogicNode.class);
    }

    @Test
    public void testOnConnectedInvokesConnector() {
        LogicNode node = new ComponentNode<Object>(componentMock, connectorMock);

        verifyZeroInteractions(connectorMock);

        node.onConnected(parentMock);

        verify(connectorMock).onConnected(same(componentMock), same(node));
    }

    @Test
    public void testOnDisconnectedInvokesConnector() {
        LogicNode node = new ComponentNode<Object>(componentMock, connectorMock);

        node.onConnected(parentMock);
        node.onDisconnected(parentMock);

        verify(connectorMock).onDisconnected(same(componentMock), same(node));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetParentThrowsWhenNotConnected() {
        LogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.getParent();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnDisconnectedThrowsWhenWrongParentReferencePassed() {
        LogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock);
        node.onDisconnected(mock(LogicNode.class));
    }

    @Test
    public void testGetParentWorksWhenConnected() {
        LogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock);
        assertSame(parentMock, node.getParent());
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowsWhenConnectorDisconnectsItOnConnect() {
        final LogicNode node = new ComponentNode<Object>(componentMock, connectorMock);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                node.onDisconnected(parentMock);
                return null;
            }
        }).when(connectorMock).onConnected(any(), any(LogicNode.class));

        node.onConnected(parentMock);
    }

    @Test(expected = IllegalStateException.class)
    public void testOnConnectThrowsWhenConnectedTwice() {
        final LogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock);
        node.onConnected(parentMock);
    }

    @Test
    public void testReturnComponent() {
        LogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock);
        assertSame(componentMock, node.getComponent());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetThrows() {
        LogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock);

        node.get(mock(Id.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetOrAddThrows() {
        NodeFactory<Object> factoryMock = mock(NodeFactory.class);

        LogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock);

        try {
            node.getOrAdd(mock(Id.class), factoryMock, null);
        } finally {
            verifyZeroInteractions(factoryMock);
        }
    }

    @Test
    public void testRemoveIdHasNoEffect() {
        LogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock);

        node.remove(mock(Id.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveChildThrows() {
        LogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock);

        node.remove(mock(LogicNode.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testOnDisconnectedThrowsWhenNotConnected() {
        LogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onDisconnected(parentMock);
    }

    @Test
    public void testAcceptVisitor() {
        LogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock);

        NodeVisitor visitor = mock(NodeVisitor.class);
        node.accept(visitor);

        verify(visitor).visitComponent(same(componentMock));

        verifyNoMoreInteractions(visitor);
    }
}
