package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.integration_tests.tree_population.components;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeAttachmentListener;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Optional;
import org.jetbrains.annotations.NotNull;

@Component(name = "component-1", kind = "test", modules = {TreePopulationITModule.class})
public class Component1 implements NodeAttachmentListener {
    private LogicNode sibling;

    @Optional
    public String siblingName = "sibling";

    @Override
    public void onAttached(@NotNull LogicNode node) {
        sibling = node.getParent().get(node.getTreeContext().getIdSet().get(siblingName));
    }

    @Override
    public void onDetached(@NotNull LogicNode node) {

    }

    public LogicNode getSibling() {
        return sibling;
    }
}
