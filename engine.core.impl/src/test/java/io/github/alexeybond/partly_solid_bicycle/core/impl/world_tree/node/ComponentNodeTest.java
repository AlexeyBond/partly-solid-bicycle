package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.*;
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
    private Id<LogicNode> idMock;

    @Before
    public void setUp() {
        connectorMock = mock(ComponentConnector.class);
        componentMock = mock(Object.class);
        parentMock = mock(LogicNode.class);
        idMock = mock(Id.class);
    }

    @Test
    public void testOnConnectedInvokesConnector() {
        ChildLogicNode node = new ComponentNode<Object>(componentMock, connectorMock);

        verifyZeroInteractions(connectorMock);

        node.onConnected(parentMock, idMock);

        verify(connectorMock).onConnected(same(componentMock), same(node), same(idMock));
    }

    @Test
    public void testOnDisconnectedInvokesConnector() {
        ChildLogicNode node = new ComponentNode<Object>(componentMock, connectorMock);

        node.onConnected(parentMock, idMock);
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
        ChildLogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock, idMock);
        node.onDisconnected(mock(LogicNode.class));
    }

    @Test
    public void testGetParentWorksWhenConnected() {
        ChildLogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock, idMock);
        assertSame(parentMock, node.getParent());
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowsWhenConnectorDisconnectsItOnConnect() {
        final ChildLogicNode node = new ComponentNode<Object>(componentMock, connectorMock);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                node.onDisconnected(parentMock);
                return null;
            }
        }).when(connectorMock).onConnected(any(), any(LogicNode.class), any(Id.class));

        node.onConnected(parentMock, idMock);
    }

    @Test(expected = IllegalStateException.class)
    public void testOnConnectThrowsWhenConnectedTwice() {
        final ChildLogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock, idMock);
        node.onConnected(parentMock, idMock);
    }

    @Test
    public void testReturnComponent() {
        ChildLogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock, idMock);
        assertSame(componentMock, node.getComponent());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetThrows() {
        ChildLogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock, idMock);

        node.get(mock(Id.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetOrAddThrows() {
        NodeFactory<Object> factoryMock = mock(NodeFactory.class);

        ChildLogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock, idMock);

        try {
            node.getOrAdd(mock(Id.class), factoryMock, null);
        } finally {
            verifyZeroInteractions(factoryMock);
        }
    }

    @Test
    public void testRemoveIdHasNoEffect() {
        ChildLogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock, idMock);

        node.remove(mock(Id.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveChildThrows() {
        ChildLogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock, idMock);

        node.remove(mock(LogicNode.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testOnDisconnectedThrowsWhenNotConnected() {
        ChildLogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onDisconnected(parentMock);
    }

    @Test
    public void testAcceptVisitor() {
        ChildLogicNode node = new ComponentNode<Object>(componentMock, connectorMock);
        node.onConnected(parentMock, idMock);

        NodeVisitor visitor = mock(NodeVisitor.class);
        node.accept(visitor);

        verify(visitor).visitComponent(same(componentMock));

        verifyNoMoreInteractions(visitor);
    }
}
