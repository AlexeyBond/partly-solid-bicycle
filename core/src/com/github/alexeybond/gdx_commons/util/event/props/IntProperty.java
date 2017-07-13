package com.github.alexeybond.gdx_commons.util.event.props;

/**
 *
 */
public class IntProperty extends Property {
    private int value;

    public IntProperty(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }

    public boolean set(int value) {
        return setSilently(value) && trigger();
    }

    public boolean setSilently(int value) {
        if (value != this.value) {
            this.value = value;
            return true;
        }

        return false;
    }

    public static IntProperty make(int value) {
        return new IntProperty(value);
    }

    public static IntProperty make() {
        return make(0);
    }

    @Override
    public String[] dump() {
        return new String[] {String.valueOf(value)};
    }

    @Override
    public void load(String[] value) {
        set(Integer.valueOf(value[0]));
    }
}
