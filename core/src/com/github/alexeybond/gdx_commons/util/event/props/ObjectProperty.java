package com.github.alexeybond.gdx_commons.util.event.props;

import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventFactory;
import com.github.alexeybond.gdx_commons.util.event.EventFactoryProvider;

import java.util.Arrays;

/**
 *
 */
public class ObjectProperty<T> extends Property {
    private final static class Factory implements EventFactory {
        private Object value;

        private <TT> EventFactory<ObjectProperty<TT>> with(TT value) {
            this.value = value;
            return this;
        }

        @Override
        public Event create() {
            return new ObjectProperty(value);
        }
    }

    private static final EventFactoryProvider<Factory> factoryProvider
            = new EventFactoryProvider<Factory>(new Factory());

    /**
     * Interface for a strategy converting serialized property value to object.
     */
    public interface Loader<T> {
        T load(String[] value);
    }

    public static final Loader IOC_LOADER = new Loader() {
        @Override
        public Object load(String[] value) {
            return IoC
                    .resolveS(value[0])
                    .resolve(Arrays.copyOfRange(value, 1, value.length, Object[].class));
        }
    };

    public static <T> Loader<T> iocLoader() {
        return IOC_LOADER;
    }

    public static final Loader<String> STRING_LOADER = new Loader<String>() {
        @Override
        public String load(String[] value) {
            return value[0];
        }
    };

    private T value;
    private String[] serialValue;
    private Loader<T> loader = iocLoader();

    private ObjectProperty(T value) {
        this.value = value;
    }

    public void set(T value) {
        if (setSilently(value)) {
            trigger();
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

    public static <T> EventFactory<ObjectProperty<T>> make(T value) {
        return factoryProvider.get().with(value);
    }

    public static <T> EventFactory<ObjectProperty<T>> make() {
        return make(null);
    }

    public ObjectProperty<T> use(Loader<T> loader) {
        this.loader = loader;
        return this;
    }

    @Override
    public String[] dump() {
        // Assume object is not serializable unless it was set on deserialization
        return serialValue;
    }

    @Override
    public void load(String[] value) {
        set(loader.load(value));
        serialValue = value;
    }
}
