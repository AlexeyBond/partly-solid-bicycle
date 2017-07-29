package com.github.alexeybond.partly_solid_bicycle.util.event.props;

import com.github.alexeybond.partly_solid_bicycle.util.event.EventFactory;
import com.github.alexeybond.partly_solid_bicycle.util.event.EventFactoryProvider;

/**
 *
 */
public class FloatProperty extends Property {
    private final static class Factory implements EventFactory<FloatProperty> {
        private float value;

        private Factory with(float value) {
            this.value = value;
            return this;
        }

        @Override
        public FloatProperty create() {
            return new FloatProperty(value);
        }
    }

    private final static EventFactoryProvider<Factory> factoryProvider
            = new EventFactoryProvider<Factory>(new Factory());

    private float value;

    private FloatProperty(float value) {
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

    public static EventFactory<FloatProperty> make(float value) {
        return factoryProvider.get().with(value);
    }

    public static EventFactory<FloatProperty> make() {
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
