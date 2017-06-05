package com.github.alexeybond.gdx_commons.util.event;

/**
 *
 */
public interface EventListener<TInitiator, TEvent extends Event<TInitiator>> {
    boolean onTriggered(TInitiator initiator, TEvent event);
}
