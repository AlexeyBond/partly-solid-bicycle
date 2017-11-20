package io.github.alexeybond.partly_solid_bicycle.core.impl.app.state;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ApplicationState;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ApplicationState Application state} implementation that maintains a current state reference
 * considering values returned by {@link #next()} method of current state.
 * <p>
 * <p>
 * {@link #next()} method of a {@link MainState} always returns the same state. So there may be no
 * more implementations of logic handling values returned by {@link #next()}: any state may be just
 * wrapped into {@link MainState} that will handle state changes.
 * </p>
 */
public final class MainState implements ApplicationState {
    @NotNull
    private ApplicationState state;

    public MainState(@NotNull ApplicationState initial) {
        this.state = initial;
    }

    private void skipStates() {
        ApplicationState prev, next;
        for (; ; ) {
            prev = state;

            // If there are any other calls to #next()
            // then probably something has gone wrong
            next = prev.next();

            if (next == prev) {
                return;
            }

            prev.pause();
            next.resume();

            state = next;
        }
    }

    /**
     * Always returns {@code this}.
     *
     * @return the next state, always {@code this} in this implementation
     */
    @NotNull
    @Override
    public ApplicationState next() {
        return this;
    }

    @Override
    public void runFrame() {
        skipStates();
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
