package com.github.alexeybond.gdx_commons.game.event.props;

import com.github.alexeybond.gdx_commons.game.event.Event;

/**
 *
 */
public class BooleanProperty<TInitiator> extends Event<TInitiator> {
    private boolean value;

    public BooleanProperty(boolean value) {
        super(0);
        this.value = value;
    }

    public boolean get() {
        return value;
    }

    public void set(TInitiator initiator, boolean value) {
        if (this.value != value) {
            this.value = value;
            trigger(initiator);
        }
    }

    public boolean setSilent(boolean value) {
        if (value != this.value) {
            this.value = value;
            return true;
        }

        return false;
    }

    public static <T> BooleanProperty<T> make(boolean value) {
        return new BooleanProperty<T>(value);
    }
}
