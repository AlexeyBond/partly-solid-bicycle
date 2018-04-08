package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory.GenericFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import org.jetbrains.annotations.NotNull;

public interface ScreenContext {
    void push(@NotNull String label, @NotNull GenericFactory<Screen, ScreenContext> factory);

    void replace(@NotNull String label, @NotNull GenericFactory<Screen, ScreenContext> factory);

    void pop();

    void pop(@NotNull String toLabel);

    /**
     * @return root node of the screen
     */
    @NotNull
    LogicNode getScreenRoot();

    @NotNull
    String getLabel();
}
