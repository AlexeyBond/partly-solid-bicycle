package com.github.alexeybond.partly_solid_bicycle.application;

/**
 * Interface for a service managing loading process.
 *
 * <p>
 * Loading process may include asset loading and some other actions (create game world, connect server, etc.) which
 * may take more time than one frame.
 * </p>
 */
public interface LoadProgressManager {
    /**
     * Check if all tasks are completed.
     */
    boolean isCompleted();

    /**
     * Load process progress in range [0, 1].
     */
    float getProgress();

    /**
     * Run at most one of tasks that need to run.
     */
    void runNext();

    /**
     * Add a task to be executed every time it needs to be executed.
     *
     * <p>
     * Progress manager will check if the task should run every time it's possible to check.
     * </p>
     */
    void addRepeatable(LoadTask task);

    /**
     * Add a task to be executed once.
     *
     * <p>
     * Tasks added by this method are executed when all tasks added by {@link #addRepeatable(LoadTask)} are done.
     * Tasks are executed in the order they are added. The next task will not run until the previous one is done.
     * </p>
     */
    void addOnce(LoadTask task);

    /**
     * Remove a task.
     */
    void remove(LoadTask task);

    /**
     * Get a text message for the latest executed task or some default message if all tasks are done.
     */
    String getMessage();
}
