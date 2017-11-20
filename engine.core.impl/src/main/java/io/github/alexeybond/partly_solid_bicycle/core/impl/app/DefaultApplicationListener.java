package io.github.alexeybond.partly_solid_bicycle.core.impl.app;

import com.badlogic.gdx.ApplicationListener;
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.state.MainState;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ApplicationState;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCContainer;
import org.jetbrains.annotations.NotNull;

public class DefaultApplicationListener implements ApplicationListener {
    @NotNull
    private final MainState state;

    @NotNull
    private final IoCContainer container;

    private void setupContext() {
        IoC.use(container);
    }

    public DefaultApplicationListener(
            @NotNull ApplicationState initialState,
            @NotNull IoCContainer container) {
        this.state = new MainState(initialState);
        this.container = container;
    }

    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        setupContext();
        // As `state` is always instance of `MainState` it's `#next()` method always returns the same state
        state.runFrame();
    }

    @Override
    public void pause() {
        setupContext();
        state.pause();
    }

    @Override
    public void resume() {
        setupContext();
        state.resume();
    }

    @Override
    public void dispose() {
        setupContext();
        state.dispose();
    }
}
