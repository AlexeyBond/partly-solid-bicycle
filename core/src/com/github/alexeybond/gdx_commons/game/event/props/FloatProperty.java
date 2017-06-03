package com.github.alexeybond.gdx_commons.game.event.props;

/**
 *
 */
public class FloatProperty<TInitiator> extends Property<TInitiator> {
    private float value;

    public FloatProperty(float value) {
        this.value = value;
    }

    public float get() {
        return value;
    }

    public boolean set(TInitiator initiator, float value) {
        // No change check as there is no sense comparing floats
        this.value = value;
        return trigger(initiator);
    }

    public boolean setSilently(float value) {
        this.value = value;
        return true;
    }

    public static <T> FloatProperty<T> make(float value) {
        return new FloatProperty<T>(value);
    }

    public static <T> FloatProperty<T> make() {
        return make(0);
    }
}
