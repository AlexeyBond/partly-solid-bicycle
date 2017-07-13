package com.github.alexeybond.gdx_commons.util.event.props;

import com.badlogic.gdx.math.Vector2;

/**
 *
 */
public class Vec2Property extends Property {
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

    public static Vec2Property make(float x, float y) {
        return new Vec2Property(x, y);
    }

    public static Vec2Property make(Vector2 value) {
        return new Vec2Property(value);
    }

    public static Vec2Property make() {
        return new Vec2Property();
    }
}
