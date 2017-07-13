package com.github.alexeybond.gdx_commons.util.event.props;

/**
 *
 */
public class FloatProperty extends Property {
    private float value;

    public FloatProperty(float value) {
        this.value = value;
    }

    public float get() {
        return value;
    }

    public boolean set(float value) {
        // No change check as there is no sense comparing floats
        this.value = value;
        return trigger();
    }

    public boolean setSilently(float value) {
        this.value = value;
        return true;
    }

    public static <T> FloatProperty make(float value) {
        return new FloatProperty(value);
    }

    public static <T> FloatProperty make() {
        return make(0);
    }

    @Override
    public String[] dump() {
        return new String[] {String.valueOf(value)};
    }

    @Override
    public void load(String[] value) {
        set(Float.valueOf(value[0]));
    }
}
