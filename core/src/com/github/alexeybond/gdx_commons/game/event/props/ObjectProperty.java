package com.github.alexeybond.gdx_commons.game.event.props;

/**
 *
 */
public class ObjectProperty<T, TInitiator> extends Property<TInitiator> {
    private T value;

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
}
