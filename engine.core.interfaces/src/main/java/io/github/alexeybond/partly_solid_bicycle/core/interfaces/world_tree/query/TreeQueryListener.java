package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.query;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import org.jetbrains.annotations.NotNull;

public interface TreeQueryListener<TState, TResult> {
    TState start();

    TState processResult(TState currentState, @NotNull LogicNode node);

    boolean shouldContinue(TState currentState);

    TResult finish(TState currentState);
}
