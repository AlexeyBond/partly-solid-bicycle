package com.github.alexeybond.gdx_commons.util.event;

/**
 *
 */
public interface EventListener<TEvent extends Event> {
    boolean onTriggered(TEvent event);
}
