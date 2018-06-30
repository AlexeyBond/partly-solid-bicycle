package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.path;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNodePath;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class StepPath implements LogicNodePath {
    @NotNull
    private final LogicNodePath basePath;

    @NotNull
    private final Object stepName;

    public StepPath(@NotNull LogicNodePath basePath, @NotNull Object stepName) {
        this.basePath = basePath;
        this.stepName = stepName;
    }

    @NotNull
    @Override
    public LogicNode lookup(@NotNull LogicNode start) throws NoSuchElementException {
        return basePath.lookup(start).get(start.getTreeContext().getIdSet().get(stepName));
    }

    @NotNull
    @Override
    public <A> LogicNode lookupOrAdd(@NotNull LogicNode start, @NotNull NodeFactory<A> factory, A argument) {
        return basePath.lookup(start).getOrAdd(
                start.getTreeContext().getIdSet().get(stepName),
                factory,
                argument
        );
    }
}
