package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory.GenericFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import org.jetbrains.annotations.NotNull;

public interface ScreenContext {
    /**
     * Push new screen on top of some exist screen. All the screens on top of that screen will be disposed.
     * <p>
     * Use <pre>context.push(context.getLabel(), ..., ...)</pre> to push a screen on top of current screen.
     *
     * @param label    label of the screen to push new screen on top of
     * @param newLabel label of the new screen
     * @param factory  factory to create the new screen with
     */
    void push(
            @NotNull String label,
            @NotNull String newLabel,
            @NotNull GenericFactory<Screen, ScreenContext> factory);

    /**
     * Replace an exist screen by a new screen. All screens on top of the replaced screen will be disposed.
     * <p>
     * Use <pre>context.replace(context.getLabel(), ..., ...)</pre> to replace the current screen.
     *
     * @param label    label of the screen to replace
     * @param newLabel label of the new screen
     * @param factory  factory to create the new screen with
     */
    void replace(
            @NotNull String label,
            @NotNull String newLabel,
            @NotNull GenericFactory<Screen, ScreenContext> factory);

    /**
     * Remove current screen from stack
     */
    void pop();

    /**
     * Remove all screens until the one with given label from stack.
     *
     * @param toLabel label of the topmost screen that should not be disposed
     */
    void pop(@NotNull String toLabel);

    /**
     * @return root node of the screen
     */
    @NotNull
    LogicNode getScreenRoot();

    /**
     * @return label of current screen
     */
    @NotNull
    String getLabel();
}
