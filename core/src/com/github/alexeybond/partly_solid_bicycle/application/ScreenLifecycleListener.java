package com.github.alexeybond.partly_solid_bicycle.application;

/**
 *
 */
public interface ScreenLifecycleListener {
    void update(float dt);

    /**
     * Called when screen is made current.
     * @param prev    previous screen or {@code null} if this screen is the initial screen
     */
    void enter(Screen prev);

    /**
     * Called when another screen is made current after this one.
     * @param next    the next current screen
     */
    void leave(Screen next);

    void pause();

    void unpause();
}
