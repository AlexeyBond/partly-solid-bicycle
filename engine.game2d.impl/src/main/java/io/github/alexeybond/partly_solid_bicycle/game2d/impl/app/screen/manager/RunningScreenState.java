package io.github.alexeybond.partly_solid_bicycle.game2d.impl.app.screen.manager;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ApplicationState;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.Screen;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.RenderTarget;
import org.jetbrains.annotations.NotNull;

class RunningScreenState implements ApplicationState {
    @NotNull
    private final Screen screen;

    @NotNull
    private final RenderTarget renderTarget;

    @NotNull
    private final LogicNode screenNode;

    RunningScreenState(
            @NotNull Screen screen,
            @NotNull RenderTarget renderTarget,
            @NotNull LogicNode screenNode) {
        this.screen = screen;
        this.renderTarget = renderTarget;
        this.screenNode = screenNode;
    }

    @NotNull
    @Override
    public ApplicationState next() {
        return this;
    }

    @Override
    public void runFrame() {
        screen.render(renderTarget);
    }

    @Override
    public void pause() {
        screen.pause();
    }

    @Override
    public void resume() {
        screen.resume();
    }

    @Override
    public void dispose() {
        try {
            screen.dispose();
        } finally {
            screenNode.getParent().remove(screenNode);
        }
    }
}
