package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import generated.io.github.alexeybond.partly_solid_bicycle.test.annotation_processing.Component10$$_companionOwner;
import generated.io.github.alexeybond.partly_solid_bicycle.test.annotation_processing.Component9_connector;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ComponentConnector;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ConnectorCompanionTest {
    @Test
    public void doTestExplicitListener() {
        LogicNode node = mock(LogicNode.class);
        Id<LogicNode> id = mock(Id.class);
        Component9 component = new Component9();
        ComponentConnector<Component9> connector = Component9_connector.RESOLVER.resolve(component);

        assertEquals(0, component.balance);

        connector.onConnected(component, node, id);

        assertEquals(1, component.balance);

        connector.onDisconnected(component, node);

        assertEquals(0, component.balance);
    }

    @Test(expected = RuntimeException.class)
    public void doTestNoConnectorForNodeClass() {
        Component10$$_companionOwner.COMPANIONS.resolve("connector");
    }
}
