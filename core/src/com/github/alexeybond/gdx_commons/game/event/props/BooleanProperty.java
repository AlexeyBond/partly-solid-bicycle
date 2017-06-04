package com.github.alexeybond.gdx_commons.game.event.props;

/**
 *
 */
public class BooleanProperty<TInitiator> extends Property<TInitiator> {
    private boolean value;

    public BooleanProperty(boolean value) {
        this.value = value;
    }

    public boolean get() {
        return value;
    }

    public boolean set(TInitiator initiator, boolean value) {
        return setSilently(value) && trigger(initiator);
    }

    public boolean setSilently(boolean value) {
        if (value != this.value) {
            this.value = value;
            return true;
        }

        return false;
    }

    public static <T> BooleanProperty<T> make(boolean value) {
        return new BooleanProperty<T>(value);
    }

    public static <T> BooleanProperty<T> make() {
        return make(false);
    }

    @Override
    public String[] dump() {
        return new String[] {String.valueOf(value)};
    }

    @Override
    public void load(TInitiator initiator, String[] value) {
        set(initiator, Boolean.valueOf(value[0]));
    }
}
