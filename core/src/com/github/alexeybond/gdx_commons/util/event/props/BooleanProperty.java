package com.github.alexeybond.gdx_commons.util.event.props;

/**
 *
 */
public class BooleanProperty extends Property {
    private boolean value;

    public BooleanProperty(boolean value) {
        this.value = value;
    }

    public boolean get() {
        return value;
    }

    public boolean set(boolean value) {
        return setSilently(value) && trigger();
    }

    public boolean setSilently(boolean value) {
        if (value != this.value) {
            this.value = value;
            return true;
        }

        return false;
    }

    public static BooleanProperty make(boolean value) {
        return new BooleanProperty(value);
    }

    public static BooleanProperty make() {
        return make(false);
    }

    @Override
    public String[] dump() {
        return new String[] {String.valueOf(value)};
    }

    @Override
    public void load(String[] value) {
        set(Boolean.valueOf(value[0]));
    }
}
