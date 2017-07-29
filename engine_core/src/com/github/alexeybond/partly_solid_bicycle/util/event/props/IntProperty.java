package com.github.alexeybond.partly_solid_bicycle.util.event.props;

import com.github.alexeybond.partly_solid_bicycle.util.event.EventFactory;
import com.github.alexeybond.partly_solid_bicycle.util.event.EventFactoryProvider;

/**
 *
 */
public class IntProperty extends Property {
    private static final class Factory implements EventFactory<IntProperty> {
        private int value;

        private Factory with(int value) {
            this.value = value;
            return this;
        }

        @Override
        public IntProperty create() {
            return new IntProperty(value);
        }
    }

    private final static EventFactoryProvider<Factory> factoryProvider
            = new EventFactoryProvider<Factory>(new Factory());

    private int value;

    private IntProperty(int value) {
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

    public static EventFactory<IntProperty> make(int value) {
        return factoryProvider.get().with(value);
    }

    public static EventFactory<IntProperty> make() {
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
