package io.github.alexeybond.partly_solid_bicycle.core.impl.app.state;

import com.badlogic.gdx.utils.Queue;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ApplicationState;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ApplicationState} that has additional method to make another states run before next run of the wrapped
 * state.
 */
public class QueueState implements ApplicationState {
    @NotNull
    private final MainState delegate;

    @NotNull
    private Queue<ApplicationState> queue = new Queue<ApplicationState>(1);

    public QueueState(@NotNull ApplicationState delegate) {
        this.delegate = new MainState(delegate);
    }

    public void enqueue(@NotNull ApplicationState state) {
        queue.addLast(state);
    }

    @NotNull
    @Override
    public ApplicationState next() {
        if (0 == queue.size) return this;
        return queue.removeFirst();
    }

    @Override
    public void runFrame() {
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
