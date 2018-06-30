package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.path;

import io.github.alexeybond.partly_solid_bicycle.core.impl.test_utils.TestUtils;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory.NodeFactories;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.NullNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PathParserTest {
    private IdSet<LogicNode> is;
    private LogicNode root;

    private LogicNode nodeA, nodeB, nodeB1, nodeB2;

    @Before
    public void setUp() {
        root = TestUtils.createRoot();
        is = root.getTreeContext().getIdSet();

        /*
         * /
         * /a
         * /b
         * /b/1
         * /b/2
         */
        nodeA = root.getOrAdd(is.get("a"), NodeFactories.EMPTY_GROUP, null);
        nodeB = root.getOrAdd(is.get("b"), NodeFactories.EMPTY_GROUP, null);
        nodeB1 = nodeB.getOrAdd(is.get("1"), NodeFactories.EMPTY_GROUP, null);
        nodeB2 = nodeB.getOrAdd(is.get("2"), NodeFactories.EMPTY_GROUP, null);
    }

    @Test
    public void testRelativeBasePath() {
        assertSame(
                nodeB,
                PathParser.parseString(".").lookup(nodeB)
        );
        assertSame(
                nodeB,
                PathParser.parseString("").lookup(nodeB)
        );
    }

    @Test
    public void testRootPath() {
        assertSame(
                root,
                PathParser.parseString("/").lookup(root)
        );
        assertSame(
                root,
                PathParser.parseString("/").lookup(nodeB1)
        );
        assertSame(
                root,
                PathParser.parseString("/").lookupOrAdd(root, NodeFactories.NULL, null)
        );
    }

    @Test
    public void testParentPath() {
        assertSame(
                nodeB,
                PathParser.parseString("..").lookup(nodeB1)
        );
        assertSame(
                nodeB,
                PathParser.parseString("..").lookupOrAdd(nodeB1, NodeFactories.NULL, null)
        );
    }

    @Test
    public void testStepLookup() {
        assertSame(
                nodeB,
                PathParser.parseString("b").lookup(root)
        );
    }

    @Test
    public void testStepAdd() {
        LogicNode res = PathParser.parseString("0").lookupOrAdd(nodeB, NodeFactories.NULL, null);

        assertSame(nodeB, res.getParent());
        assertTrue(res instanceof NullNode);
    }

    @Test
    public void testComplexLookup() {
        assertSame(
                nodeB2,
                PathParser.parseString("../b/2").lookup(nodeA)
        );
        assertSame(
                nodeB2,
                PathParser.parseString("/b/2").lookup(nodeA)
        );
    }
}