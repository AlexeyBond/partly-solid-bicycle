package com.github.alexeybond.partly_solid_bicycle.util.event;

/**
 * A factory that creates events.
 *
 * Used to create events lazily.
 */
public interface EventFactory<T extends Event> {
    T create();
}
