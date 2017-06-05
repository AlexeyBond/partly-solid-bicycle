package com.github.alexeybond.gdx_commons.util.event.props;

/**
 *
 */
public class IntProperty<TInitiator> extends Property<TInitiator> {
    private int value;

    public IntProperty(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }

    public boolean set(TInitiator initiator, int value) {
        return setSilently(value) && trigger(initiator);
    }

    public boolean setSilently(int value) {
        if (value != this.value) {
            this.value = value;
            return true;
        }

        return false;
    }

    public static <T> IntProperty<T> make(int value) {
        return new IntProperty<T>(value);
    }

    public static <T> IntProperty<T> make() {
        return make(0);
    }

    @Override
    public String[] dump() {
        return new String[] {String.valueOf(value)};
    }

    @Override
    public void load(TInitiator initiator, String[] value) {
        set(initiator, Integer.valueOf(value[0]));
    }
}
