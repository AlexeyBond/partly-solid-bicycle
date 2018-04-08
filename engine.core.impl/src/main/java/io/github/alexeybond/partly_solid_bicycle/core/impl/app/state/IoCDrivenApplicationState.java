package io.github.alexeybond.partly_solid_bicycle.core.impl.app.state;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ApplicationState;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import org.jetbrains.annotations.NotNull;

public class IoCDrivenApplicationState implements ApplicationState {
    @NotNull
    private final ApplicationState terminationState;

    /**
     * @param terminationState the state that should be activated when the state provided by IoC
     *                         dependency decides to terminate application
     */
    public IoCDrivenApplicationState(@NotNull ApplicationState terminationState) {
        this.terminationState = terminationState;
    }

    @NotNull
    @Override
    public ApplicationState next() {
        return IoC.resolve("initial application state", terminationState);
    }

    @Override
    public void runFrame() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
