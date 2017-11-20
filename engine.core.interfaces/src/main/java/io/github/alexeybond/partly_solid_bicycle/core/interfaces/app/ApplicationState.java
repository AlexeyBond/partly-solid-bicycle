package io.github.alexeybond.partly_solid_bicycle.core.interfaces.app;

import org.jetbrains.annotations.NotNull;

/**
 * Application state. Defines behaviour of application when is active.
 * <p>
 * <p>
 * State diagram of {@link ApplicationState}:
 * <p>
 * <pre>
 *      |
 *      v
 *    PAUSED  <------> ACTIVE
 *      |
 *      |
 *      v
 *   DISPOSED
 *  </pre>
 * <p>
 * <ul>
 * <li>
 * {@code PAUSED} - state is not active. Paused state may be disposed (by {@link #dispose()} call)
 * or made active (by {@link #resume()} call).
 * Calls to {@link #next()} or {@link #runFrame()} will cause undefined behaviour.
 * </li>
 * <li>
 * {@code ACTIVE} - state is active and it's ready for {@link #next()} and probably {@link #runFrame()}
 * calls.
 * Active state cannot be disposed: {@link #dispose()} call will cause undefined behaviour.
 * </li>
 * <li>
 * {@code DISPOSED} - state is disposed, any associated resources are released.
 * Calls to any methods of disposed cause undefined behaviour.
 * </li>
 * </ul>
 * </p>
 */
public interface ApplicationState {
    /**
     * Returns the next state.
     * <p>
     * <p>
     * Unless explicitly documented the sate is not guaranteed to behave correct if {@link #runFrame()}
     * is called when preceding call to it's {@code #next()} method could return a state other than the
     * state it was called on (i.e. if the method was not called immediately before {@link #runFrame()}
     * or it's return value was ignored).
     * </p>
     * <p>
     * <p>
     * State returned by this method is expected to be in {@code PAUSED} state unless it is the same state
     * this method was called on.
     * </p>
     * <p>
     * <p>
     * The state this method is called on should be in {@code ACTIVE} state. Otherwise call will cause
     * undefined behaviour.
     * </p>
     *
     * @return the next state
     */
    @NotNull
    ApplicationState next();

    /**
     * Render one frame.
     * <p>
     * <p>
     * There is no separate {@code update()} method and there is no "delta time" parameter
     * passed so update process is fully controlled by state implementation and time delta
     * should be computed by state implementation or requested from some global service.
     * </p>
     * <p>
     * <p>
     * Causes UB when called not in {@code ACTIVE} state.
     * </p>
     */
    void runFrame();

    /**
     * Change state of this state to {@code PAUSED}.
     * <p>
     * <p>
     * Causes UB when called not in {@code ACTIVE} state.
     * </p>
     *
     * @see ApplicationState
     */
    void pause();

    /**
     * Change state of this state to {@code ACTIVE}.
     * <p>
     * <p>
     * Causes UB when called not in {@code PAUSED} state.
     * </p>
     *
     * @see ApplicationState
     */
    void resume();

    /**
     * Change state of this state to {@code DISPOSED}.
     * <p>
     * <p>
     * Causes UB when called in {@code ACTIVE} state.
     * </p>
     *
     * @see ApplicationState
     */
    void dispose();
}
