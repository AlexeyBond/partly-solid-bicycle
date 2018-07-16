package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import generated.io.github.alexeybond.partly_solid_bicycle.test.annotation_processing.*;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory.DeclarativeComponentNodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory.NodeFactories;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ComponentConnector;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.test.annotation_processing.test_utils.TestUtils;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConnectorCompanionTest {
    @Test
    public void doTestExplicitListener() {
        LogicNode node = mock(LogicNode.class);
        Id<LogicNode> id = mock(Id.class);
        Component9$_impl component = new Component9$_impl();
        ComponentConnector<Component9$_impl> connector = Component9_connector.RESOLVER.resolve(component);

        assertEquals(0, component.balance);

        connector.onConnected(component, node, id);

        assertEquals(1, component.balance);

        connector.onDisconnected(component, node);

        assertEquals(0, component.balance);
    }

    @Test(expected = RuntimeException.class)
    public void doTestNoConnectorForNodeClass() {
        Component10$_impl.getClassCompanionResolver().resolve("connector");
    }

    @Test
    public void doTestNodeBinding() {
        LogicNode root = TestUtils.createRoot();
        IdSet<LogicNode> is = root.getTreeContext().getIdSet();

        LogicNode nodeA = root.getOrAdd(is.get("nodeA"), NodeFactories.NULL, null);
        LogicNode nodeB = root.getOrAdd(is.get("nodeBB"), NodeFactories.NULL, null);
        LogicNode nodeC = root.getOrAdd(is.get("nodeC"), NodeFactories.NULL, null);

        LogicNode x = root.getOrAdd(
                is.get("x"),
                new DeclarativeComponentNodeFactory<Component12$_impl>(
                        Component12$_impl.getFactory(),
                        Component12_loader.RESOLVER,
                        Component12_connector.RESOLVER
                ),
                TestUtils.parseJSON("{nodeB:\"../nodeBB\"}")
        );

        Component12 componentX = x.getComponent();

        assertSame(nodeA, componentX.nodeA);
        assertSame(nodeB, componentX.nodeB);
        assertSame(nodeC, componentX.nodeC);

        root.remove(x);

        assertNull(componentX.nodeA);
        assertNull(componentX.nodeB);
        assertNull(componentX.nodeC);
    }

    @Test
    public void doTestComponentBinding() {
        LogicNode root = TestUtils.createRoot();
        IdSet<LogicNode> is = root.getTreeContext().getIdSet();

        LogicNode child = root.getOrAdd(
                is.get("child"),
                new DeclarativeComponentNodeFactory<Component13$_impl>(
                        Component13$_impl.getFactory(),
                        Component13_loader.RESOLVER,
                        Component13_connector.RESOLVER
                ),
                TestUtils.parseJSON("{}")
        );

        Component13 component = child.getComponent();

        assertSame(component, component.self);

        root.remove(child);

        assertNull(component.self);
    }

    @Test
    public void doTestEventSubscriptions() {
        LogicNode root = TestUtils.createRoot();
        IdSet<LogicNode> is = root.getTreeContext().getIdSet();

        Topic eventAMock = mock(Topic.class);
        Topic eventBMock = mock(Topic.class);
        Topic eventCMock = mock(Topic.class);

        ArgumentCaptor<Listener> listenerACaptor = ArgumentCaptor.forClass(Listener.class);
        ArgumentCaptor<Listener> listenerBCaptor = ArgumentCaptor.forClass(Listener.class);
        ArgumentCaptor<Listener> listenerCCaptor = ArgumentCaptor.forClass(Listener.class);

        root.getOrAdd(is.get("eventA"), NodeFactories.SIMPLE_COMPONENT, eventAMock);
        root.getOrAdd(is.get("eventBB"), NodeFactories.SIMPLE_COMPONENT, eventBMock);
        root.getOrAdd(is.get("eventC"), NodeFactories.SIMPLE_COMPONENT, eventCMock);

        LogicNode child = root.getOrAdd(
                is.get("child"),
                new DeclarativeComponentNodeFactory<Component14$_impl>(
                        Component14$_impl.getFactory(),
                        Component14_loader.RESOLVER,
                        Component14_connector.RESOLVER
                ),
                TestUtils.parseJSON("{eventB:\"../eventBB\"}")
        );

        verify(eventAMock, times(1)).subscribe(listenerACaptor.capture());
        verify(eventBMock, times(1)).subscribe(listenerBCaptor.capture());
        verify(eventCMock, times(1)).subscribe(listenerCCaptor.capture());
        verifyNoMoreInteractions(eventAMock, eventBMock, eventCMock);

        Component14 component = child.getComponent();

        assertEquals(0, component.aInvocations);
        assertEquals(0, component.bInvocations);
        assertEquals(0, component.cInvocations);

        listenerACaptor.getValue().receive(eventAMock, eventAMock);

        assertEquals(1, component.aInvocations);
        assertEquals(0, component.bInvocations);
        assertEquals(0, component.cInvocations);

        listenerBCaptor.getValue().receive(eventBMock, eventBMock);

        assertEquals(1, component.aInvocations);
        assertEquals(1, component.bInvocations);
        assertEquals(0, component.cInvocations);

        listenerCCaptor.getValue().receive(Integer.valueOf(42), eventCMock);

        assertEquals(1, component.aInvocations);
        assertEquals(1, component.bInvocations);
        assertEquals(1, component.cInvocations);

        root.remove(child);

        verify(eventAMock).unsubscribe(same(null), same(listenerACaptor.getValue()));
        verify(eventBMock).unsubscribe(same(null), same(listenerBCaptor.getValue()));
        verify(eventCMock).unsubscribe(same(null), same(listenerCCaptor.getValue()));
        verifyNoMoreInteractions(eventAMock, eventBMock, eventCMock);
    }
}
