package io.github.alexeybond.partly_solid_bicycle.core.impl.app.state;

import com.badlogic.gdx.Gdx;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ApplicationState;
import org.jetbrains.annotations.NotNull;

public enum TerminalStates implements ApplicationState {
    NULL,
    EXIT {
        @Override
        public void runFrame() {
            Gdx.app.exit();
        }
    },;

    @NotNull
    @Override
    public ApplicationState next() {
        return this;
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
