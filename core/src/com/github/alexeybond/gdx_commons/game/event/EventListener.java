package com.github.alexeybond.gdx_commons.game.event;

/**
 *
 */
public interface EventListener<TInitiator, TEvent extends Event<TInitiator>> {
    boolean onTriggered(TInitiator initiator, TEvent event);
}
