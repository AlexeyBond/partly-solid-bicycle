package io.github.alexeybond.partly_solid_bicycle.core.interfaces.app;

/**
 * {@link ApplicationState Application stete} that needs it's {@link #init()} method to be called
 * before the first call to {@link #runFrame()}.
 */
public interface InitializableApplicationState extends ApplicationState {
    /**
     * Initialize the state.
     */
    void init();
}
