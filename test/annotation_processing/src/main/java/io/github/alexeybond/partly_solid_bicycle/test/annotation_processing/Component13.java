package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeAttachmentListener;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.FromPath;
import org.jetbrains.annotations.NotNull;

@Component(name = "component-13", kind = "any")
public class Component13 implements NodeAttachmentListener {
    @FromPath(".")
    public Component13 self;

    private void check() {
        if (self != this) throw new AssertionError("self is missing");
    }

    @Override
    public void onAttached(@NotNull LogicNode node) {
        check();
    }

    @Override
    public void onDetached(@NotNull LogicNode node) {
        check();
    }
}
