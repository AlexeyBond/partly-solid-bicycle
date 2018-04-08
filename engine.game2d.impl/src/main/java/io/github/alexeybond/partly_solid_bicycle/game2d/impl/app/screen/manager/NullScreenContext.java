package io.github.alexeybond.partly_solid_bicycle.game2d.impl.app.screen.manager;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory.GenericFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.Screen;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenContext;
import org.jetbrains.annotations.NotNull;

class NullScreenContext implements ScreenContext {
    private static final String LABEL = "null";

    @Override
    public void push(@NotNull String label, @NotNull String newLabel, @NotNull GenericFactory<Screen, ScreenContext> factory) {
        // TODO:: M.b. implement #push in root screen context and simplify #replace implementation using call of previous context's #push...
        throw new IllegalArgumentException("No such screen label: '" + label + "'");
    }

    @Override
    public void replace(@NotNull String label, @NotNull String newLabel, @NotNull GenericFactory<Screen, ScreenContext> factory) {
        throw new IllegalArgumentException("No such screen label: '" + label + "'");
    }

    @Override
    public void pop() {
        throw new IllegalStateException("Cannot pop null-screen");
    }

    @Override
    public void pop(@NotNull String toLabel) {
        if (toLabel.equals(LABEL)) return;

        throw new IllegalArgumentException("No such screen label: '" + toLabel + "'");
    }

    @NotNull
    @Override
    public LogicNode getScreenRoot() {
        throw new IllegalStateException("Cannot get root of null-screen");
    }

    @NotNull
    @Override
    public String getLabel() {
        return LABEL;
    }
}
