package io.github.alexeybond.partly_solid_bicycle.core.impl.app.state;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ApplicationState;
import org.jetbrains.annotations.NotNull;

public abstract class SingleActionState implements ApplicationState {
    @NotNull
    private final ApplicationState nextState;

    @NotNull
    private ApplicationState next;

    public SingleActionState(@NotNull ApplicationState nextState) {
        this.nextState = nextState;
        next = this;
    }

    @NotNull
    @Override
    public ApplicationState next() {
        return next;
    }

    @Override
    public void runFrame() {
        try {
            act();
        } finally {
            next = nextState;
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        nextState.dispose();
    }

    protected abstract void act();
}
