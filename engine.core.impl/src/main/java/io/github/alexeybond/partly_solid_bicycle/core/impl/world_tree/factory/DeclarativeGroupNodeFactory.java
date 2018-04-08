package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory;

import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver.NullChildResolver;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.GroupNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.populator.DeclarativePopulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodePopulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.adapter.NodeFactoryAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeclarativeGroupNodeFactory extends NodeFactoryAdapter<InputDataObject> {
    @NotNull
    private final NodeFactory<InputDataObject> factory;

    @NotNull
    private final String itemsField;

    public DeclarativeGroupNodeFactory(
            @NotNull NodeFactory<InputDataObject> factory,
            @NotNull String itemsField) {
        this.factory = factory;
        this.itemsField = itemsField;
    }

    @NotNull
    @Override
    public LogicNode create(@Nullable InputDataObject arg) {
        if (null == arg) throw new NullPointerException("arg");

        InputDataObject itemsData = arg.getField(itemsField);

        NodePopulator populator = new DeclarativePopulator(factory, itemsData);

        return new GroupNode(NullChildResolver.INSTANCE, populator);
    }
}
