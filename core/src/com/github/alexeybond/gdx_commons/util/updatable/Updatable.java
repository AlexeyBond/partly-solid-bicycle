package com.github.alexeybond.gdx_commons.util.updatable;

/**
 * Interface for things that have a method ({@link #update()}) that should be called periodically (probably
 * every frame) on all things in a group.
 */
public interface Updatable {
    /**
     * The update method.
     */
    void update();

    /**
     * Called before update to check if update is necessary.
     *
     * @return {@code false} if {@link #update()} method should no longer be called
     */
    boolean isAlive();
}
