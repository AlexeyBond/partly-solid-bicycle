package com.github.alexeybond.gdx_commons.game.event.props;

import com.github.alexeybond.gdx_commons.game.event.Event;

/**
 * Property is an event source holding a value. The event happens when the value is changed.
 */
public abstract class Property <TChangeInitiator> extends Event<TChangeInitiator> {
    protected Property() {
        // By-default do not create listeners array. Array will be created lazily
        super(0);
    }
}
