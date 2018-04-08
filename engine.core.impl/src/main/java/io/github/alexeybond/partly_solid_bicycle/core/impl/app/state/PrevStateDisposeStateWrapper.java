package io.github.alexeybond.partly_solid_bicycle.core.impl.app.state;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ApplicationState;
import org.jetbrains.annotations.NotNull;

/**
 * A wrapper for a {@link ApplicationState} that disposes the previous state on first call of
 * {@link #resume()}.
 */
public class PrevStateDisposeStateWrapper implements ApplicationState {
    @NotNull
    private ApplicationState next, disposable;

    @NotNull
    private final ApplicationState wrapped;

    public PrevStateDisposeStateWrapper(
            @NotNull ApplicationState wrapped,
            @NotNull ApplicationState disposable) {
        this.disposable = disposable;
        this.wrapped = wrapped;
        this.next = this;
    }

    private void disposeDisposable() {
        try {
            disposable.dispose();
        } finally {
            next = wrapped;
            disposable = TerminalStates.NULL;
        }
    }

    @NotNull
    @Override
    public ApplicationState next() {
        return next;
    }

    @Override
    public void runFrame() {
        disposeDisposable();
    }

    @Override
    public void pause() {
        wrapped.pause();
    }

    @Override
    public void resume() {
        disposeDisposable();
        wrapped.resume();
    }

    @Override
    public void dispose() {
        disposeDisposable();
        wrapped.dispose();
    }
}
