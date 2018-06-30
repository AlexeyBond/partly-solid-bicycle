package io.github.alexeybond.partly_solid_bicycle.core.impl.data.storage;

import io.github.alexeybond.partly_solid_bicycle.core.impl.event.variable.DefaultObjectVariable;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory.NodeFactories;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables.ObjectVariable;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ChildLogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.adapter.NodeFactoryAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class InitialChildFactory extends NodeFactoryAdapter<InputDataObject> {
    @NotNull
    private final Listener<ObjectVariable<InputDataObject>> listener;

    InitialChildFactory(@NotNull Listener<ObjectVariable<InputDataObject>> listener) {
        this.listener = listener;
    }

    @NotNull
    @Override
    public ChildLogicNode create(@Nullable InputDataObject arg) {
        ObjectVariable<InputDataObject> variable = new DefaultObjectVariable<InputDataObject>(1);
        variable.set(arg);
        variable.subscribe(listener);
        return NodeFactories.SIMPLE_COMPONENT.create(variable);
    }
}
