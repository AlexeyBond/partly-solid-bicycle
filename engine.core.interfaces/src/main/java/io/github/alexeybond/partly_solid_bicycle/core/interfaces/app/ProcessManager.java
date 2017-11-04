package io.github.alexeybond.partly_solid_bicycle.core.interfaces.app;

/**
 * Process manager maintains a list of repeatably executed processes and information on their ordering.
 *
 * <p>
 *  Most common use of process manager is main game loop consisting of processes registered by different
 *  systems.
 * </p>
 */
public interface ProcessManager {
    /**
     * Give process manager a hint on process execution order.
     *
     * <p>
     *  Hint consists of two process names. First of those processes should be executed
     *  before the second one.
     * </p>
     *
     * @param first  name of process that should be executed earlier
     * @param second name of process that should be executed later
     */
    void orderHint(String first, String second);

    /**
     * Add process.
     *
     * @param name     process name
     * @param runnable process runnable
     */
    void addProcess(String name, Runnable runnable);

    /**
     * Remove process.
     *
     * @param name     process name
     * @param runnable process runnable
     * @throws IllegalStateException if there was no such process
     */
    void removeProcess(String name, Runnable runnable);
}
