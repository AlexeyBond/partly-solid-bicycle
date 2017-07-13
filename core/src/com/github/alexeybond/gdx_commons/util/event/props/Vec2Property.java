package com.github.alexeybond.gdx_commons.util.event.props;

import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.gdx_commons.util.event.EventFactory;
import com.github.alexeybond.gdx_commons.util.event.EventFactoryProvider;

/**
 *
 */
public class Vec2Property extends Property {
    private static final class Factory implements EventFactory<Vec2Property> {
        private float x, y;

        private Factory with(float x, float y) {
            this.x = x;
            this.y = y;
            return this;
        }

        @Override
        public Vec2Property create() {
            return new Vec2Property(x, y);
        }
    }

    private final static EventFactoryProvider<Factory> factoryProvider
            = new EventFactoryProvider<Factory>(new Factory());

    private final Vector2 vector;

    public Vec2Property(float x, float y) {
        this.vector = new Vector2(x, y);
    }

    public Vec2Property(Vector2 val) {
        this(val.x, val.y);
    }

    public Vec2Property() {
        this(0,0);
    }

    @Override
    public String[] dump() {
        return new String[] {String.valueOf(vector.x), String.valueOf(vector.y)};
    }

    @Override
    public void load(String[] value) {
        set(Float.valueOf(value[0]), Float.valueOf(value[1]));
    }

    public boolean set(float x, float y) {
        this.vector.set(x, y);
        return trigger();
    }

    public boolean set(Vector2 value) {
        return set(value.x, value.y);
    }

    public void get(Vector2 out) {
        out.set(vector);
    }

    public Vector2 ref() {
        return vector;
    }

    public static EventFactory<Vec2Property> make(float x, float y) {
        return factoryProvider.get().with(x, y);
    }

    public static EventFactory<Vec2Property> make(Vector2 value) {
        return factoryProvider.get().with(value.x, value.y);
    }

    public static EventFactory<Vec2Property> make() {
        return factoryProvider.get().with(0, 0);
    }
}
