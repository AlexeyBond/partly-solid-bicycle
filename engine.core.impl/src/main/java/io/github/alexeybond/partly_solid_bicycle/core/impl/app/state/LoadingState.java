package io.github.alexeybond.partly_solid_bicycle.core.impl.app.state;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ApplicationState;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.LoadProgress;
import org.jetbrains.annotations.NotNull;

public class LoadingState implements ApplicationState {
    @NotNull
    private final LoadProgress progress;

    @NotNull
    private final ApplicationState next, delegate;

    public LoadingState(
            @NotNull LoadProgress progress,
            @NotNull ApplicationState next,
            @NotNull ApplicationState delegate) {
        this.progress = progress;
        this.next = next;
        this.delegate = delegate;
    }

    @NotNull
    @Override
    public ApplicationState next() {
        if (progress.isCompleted()) {
            return next;
        } else {
            return this;
        }
    }

    @Override
    public void runFrame() {
        progress.run();
        delegate.runFrame();
    }

    @Override
    public void pause() {
        delegate.pause();
    }

    @Override
    public void resume() {
        delegate.resume();
    }

    @Override
    public void dispose() {
        delegate.dispose();
    }
}
