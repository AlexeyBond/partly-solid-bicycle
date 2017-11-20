package io.github.alexeybond.partly_solid_bicycle.core.impl.app.state;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ApplicationState;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.LoadProgress;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import org.jetbrains.annotations.NotNull;

public class CheckLoadingState implements ApplicationState {
    @NotNull
    private final LoadProgress progress;

    @NotNull
    private final MainState state;

    public CheckLoadingState(
            @NotNull LoadProgress progress,
            @NotNull ApplicationState initialState) {
        this.progress = progress;
        state = new MainState(initialState);
    }

    @NotNull
    @Override
    public ApplicationState next() {
        if (progress.isCompleted()) {
            return this;
        }

        return IoC.resolve("loading state", progress, this);
    }

    @Override
    public void runFrame() {
        state.runFrame();
    }

    @Override
    public void pause() {
        state.pause();
    }

    @Override
    public void resume() {
        state.resume();
    }

    @Override
    public void dispose() {
        state.dispose();
    }
}
