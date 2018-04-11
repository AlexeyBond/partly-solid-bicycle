package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.integration_tests.tree_population;

import io.github.alexeybond.partly_solid_bicycle.core.impl.test_utils.DefaultIntegrationTest;
import io.github.alexeybond.partly_solid_bicycle.core.impl.test_utils.TestUtils;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver.NullChildResolver;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.integration_tests.tree_population.components.Component1;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.integration_tests.tree_population.components.TreePopulationITModule;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.GroupNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.SuperRootNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.populator.DeclarativePopulator;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.populator.NullPopulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodePopulator;
import io.github.alexeybond.partly_solid_bicycle.core.modules.DeclarativeNodeFactories;
import org.junit.Test;

import static org.junit.Assert.*;

public class TreePopulationIT extends DefaultIntegrationTest {
    {
        modules(
                new DeclarativeNodeFactories(),
                new TreePopulationITModule()
        );
    }

    private void verifyNode(LogicNode group) {
        IdSet<LogicNode> is = group.getTreeContext().getIdSet();

        LogicNode nodeA = group.get(is.get("a"));
        LogicNode nodeB = group.get(is.get("b"));
        LogicNode nodeC = group.get(is.get("c"));
        LogicNode nodeD = group.get(is.get("d"));

        assertTrue(nodeA.getComponent() instanceof Component1);
        assertTrue(nodeB.getComponent() instanceof Component1);
        assertTrue(nodeC.getComponent() instanceof Component1);
        assertTrue(nodeD.getComponent() instanceof Component1);

        assertNotSame(nodeA.getComponent(), nodeB.getComponent());
        assertNotSame(nodeA.getComponent(), nodeC.getComponent());
        assertNotSame(nodeA.getComponent(), nodeD.getComponent());
        assertNotSame(nodeB.getComponent(), nodeC.getComponent());
        assertNotSame(nodeB.getComponent(), nodeD.getComponent());
        assertNotSame(nodeC.getComponent(), nodeD.getComponent());

        assertSame(nodeB, nodeA.<Component1>getComponent().getSibling());
        assertSame(nodeC, nodeB.<Component1>getComponent().getSibling());
        assertSame(nodeA, nodeC.<Component1>getComponent().getSibling());
        assertSame(nodeB, nodeD.<Component1>getComponent().getSibling());
    }

    @Test
    public void doTestNodePopulation() {
        InputDataObject config = TestUtils.parseJSON(getClass(), "testData.json");

        NodeFactory<InputDataObject> factory = IoC.resolve("node factory for node kind", "test");

        IdSet<LogicNode> is = IoC.resolve("id set for node kind", "test");

        SuperRootNode superRoot = new SuperRootNode(is, new GroupNode(
                NullChildResolver.INSTANCE, NullPopulator.INSTANCE));
        LogicNode root = superRoot.getRoot();

        LogicNode group = root.getOrAdd(is.get("group"), factory, config);

        assertSame(group, root.get(is.get("group")));

        verifyNode(group);
    }

    @Test
    public void doTestLatePopulation() {
        InputDataObject config = TestUtils.parseJSON(getClass(), "testData.json");

        NodeFactory<InputDataObject> factory = IoC.resolve("node factory for node kind", "test");

        IdSet<LogicNode> is = IoC.resolve("id set for node kind", "test");

        SuperRootNode superRoot = new SuperRootNode(is, new GroupNode(
                NullChildResolver.INSTANCE, NullPopulator.INSTANCE));
        LogicNode root = superRoot.getRoot();

        NodePopulator populator = new DeclarativePopulator(factory, config.getField("items"));

        root.populate(populator);

        verifyNode(root);
    }
}
