package com.github.alexeybond.partly_solid_bicycle.util.event.props;

import com.github.alexeybond.partly_solid_bicycle.util.event.EventFactory;
import com.github.alexeybond.partly_solid_bicycle.util.event.EventFactoryProvider;

/**
 *
 */
public class BooleanProperty extends Property {
    private final static class Factory implements EventFactory<BooleanProperty> {
        private boolean value;

        private Factory with(boolean value) {
            this.value = value;
            return this;
        }

        @Override
        public BooleanProperty create() {
            return new BooleanProperty(value);
        }
    }

    private final static EventFactoryProvider<Factory> factoryProvider
            = new EventFactoryProvider<Factory>(new Factory());

    private boolean value;

    private BooleanProperty(boolean value) {
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

    public static EventFactory<BooleanProperty> make(boolean value) {
        return factoryProvider.get().with(value);
    }

    public static EventFactory<BooleanProperty> make() {
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
