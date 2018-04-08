package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeAttachmentListener;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import org.jetbrains.annotations.NotNull;

@Component(name = "component-9", kind = "any")
public class Component9 implements NodeAttachmentListener {
    public int balance = 0;

    @Override
    public void onAttached(@NotNull LogicNode node) {
        ++balance;
    }

    @Override
    public void onDetached(@NotNull LogicNode node) {
        --balance;
    }
}
