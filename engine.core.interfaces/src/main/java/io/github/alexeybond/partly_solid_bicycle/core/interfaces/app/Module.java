package io.github.alexeybond.partly_solid_bicycle.core.interfaces.app;

/**
 * Module. Initializes some application components.
 * <p>
 * {@link #init(Iterable) Initialization method} takes list of environment flags.
 * Those flags are strings marking some properties of environment the module works in.
 * Some of possible flags are:
 * <ul>
 * <li>
 * {@code "editor"} - when module is initialized within a editor application and thus must provide
 * components responsible for edit process of other component's configurations.
 * </li>
 * <li>
 * {@code "network"} - when a application is running in network mode.
 * (none of "client-only" or "server-only" may be set if both client and server run in the same application
 * or if network application architecture is not client-server)
 * </li>
 * <li>
 * {@code "client-only"} - for client-server application/game.
 * Module may skip registration of some components required only by server-side.
 * </li>
 * <li>
 * {@code "server-only"} - like {@code "client-only"} but is set on server side.
 * If this flag is set modules may skip registration of some visual components not required at server-side.
 * </li>
 * </ul>
 * </p>
 */
public interface Module {
    /**
     * Initialize the components this module provides.
     *
     * @param env list of environment flags
     */
    void init(Iterable<Object> env);

    /**
     * Shutdown the components this module provides.
     */
    void shutdown();
}
