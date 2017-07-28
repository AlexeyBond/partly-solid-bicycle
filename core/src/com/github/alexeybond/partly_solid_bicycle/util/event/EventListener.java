package com.github.alexeybond.partly_solid_bicycle.util.event;

/**
 *
 */
public interface EventListener<TEvent extends Event> {
    boolean onTriggered(TEvent event);
}
