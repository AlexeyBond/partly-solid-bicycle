package io.github.alexeybond.partly_solid_bicycle.core.impl.app.state;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ApplicationState;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.InitializableApplicationState;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ApplicationState Application state} that calls {@link InitializableApplicationState#init() #init()}
 * method of underlying {@link InitializableApplicationState} on first frame and then makes the underlying state
 * current.
 */
public class InitializingState implements ApplicationState {
    // The next state
    @NotNull
    private ApplicationState next;

    // State that should receive dispose event
    @NotNull
    private ApplicationState listening;

    @NotNull
    private final InitializableApplicationState initializable;

    public InitializingState(
            @NotNull InitializableApplicationState initializable) {
        this.initializable = initializable;

        next = this;
        listening = TerminalStates.NULL;
    }

    @NotNull
    @Override
    public ApplicationState next() {
        return next;
    }

    @Override
    public void runFrame() {
        // Set initializable state to be next
        next = initializable;

        // However dispose event may occur between frames so let initializable
        // state receive them
        listening = initializable;

        initializable.init();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        listening.dispose();
    }
}
