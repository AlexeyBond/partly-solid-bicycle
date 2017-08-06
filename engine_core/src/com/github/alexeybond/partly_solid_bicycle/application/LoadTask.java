package com.github.alexeybond.partly_solid_bicycle.application;

/**
 * Load task represents a part of application load process e.g. "load assets", "create game world".
 */
public interface LoadTask {
    /**
     * Checks if the task is already done.
     */
    boolean isDone();

    /**
     * Run a task. Progress manager will call this method in each frame until the task becomes done.
     */
    void run();

    /**
     * @return task progress
     */
    float getProgress();

    /**
     * @return maximal value returned by {@link #getProgress()}
     */
    float getMaxProgress();

    /**
     * @return message to display when this task is being executed
     */
    String getMessage();
}
