package com.github.alexeybond.gdx_commons.util.event;

/**
 * A factory that creates events.
 *
 * Used to create events lazily.
 */
public interface EventFactory<T extends Event> {
    T create();
}
