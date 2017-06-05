package com.github.alexeybond.gdx_commons.util.event;

/**
 *
 */
public interface EventsOwner<TEventsInitiator> {
    Events<TEventsInitiator> events();
}
