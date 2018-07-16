package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeAttachmentListener;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.FromAttribute;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.FromPath;
import org.jetbrains.annotations.NotNull;

@Component(name = "component-12", kind = "any")
public class Component12 implements NodeAttachmentListener {
    @FromPath("../nodeA")
    public LogicNode nodeA;

    @FromAttribute("nodeB")
    public LogicNode nodeB;

    @FromAttribute(value = "nodeC", defaultPath = "../nodeC")
    public LogicNode nodeC;

    private void checkNodes() {
        if (null == nodeA) throw new AssertionError("nodeA missing");
        if (null == nodeB) throw new AssertionError("nodeB missing");
        if (null == nodeC) throw new AssertionError("nodeC missing");
    }

    @Override
    public void onAttached(@NotNull LogicNode node) {
        checkNodes();
    }

    @Override
    public void onDetached(@NotNull LogicNode node) {
        checkNodes();
    }
}
