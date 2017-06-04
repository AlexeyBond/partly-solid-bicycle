package com.github.alexeybond.gdx_commons.game.event.props;

import com.github.alexeybond.gdx_commons.ioc.IoC;

import java.util.Arrays;

/**
 *
 */
public class ObjectProperty<T, TInitiator> extends Property<TInitiator> {
    private T value;
    private String[] serialValue;

    public ObjectProperty(T value) {
        this.value = value;
    }

    public void set(TInitiator initiator, T value) {
        if (setSilently(value)) {
            trigger(initiator);
        }
    }

    public boolean setSilently(T value) {
        if (value != this.value) {
            this.value = value;
            serialValue = null;
            return true;
        }

        return false;
    }

    public T get() {
        return value;
    }

    public static <T, TI> ObjectProperty<T, TI> make(T value) {
        return new ObjectProperty<T, TI>(value);
    }

    public static <T, TI> ObjectProperty<T, TI> make() {
        return make(null);
    }

    @Override
    public String[] dump() {
        // Assume object is not serializable unless it was set on deserialization
        return serialValue;
    }

    @Override
    public void load(TInitiator initiator, String[] value) {
        Object val = IoC
                .resolveS(value[0])
                .resolve(Arrays.copyOfRange(value, 1, value.length, Object[].class));
        set(initiator, (T) val);
        serialValue = value;
    }
}
