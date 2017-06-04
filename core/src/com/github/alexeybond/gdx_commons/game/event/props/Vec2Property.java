package com.github.alexeybond.gdx_commons.game.event.props;

import com.badlogic.gdx.math.Vector2;

/**
 *
 */
public class Vec2Property <TInitiator> extends Property<TInitiator> {
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
    public void load(TInitiator initiator, String[] value) {
        set(initiator, Float.valueOf(value[0]), Float.valueOf(value[1]));
    }

    public boolean set(TInitiator initiator, float x, float y) {
        this.vector.set(x, y);
        return trigger(initiator);
    }

    public boolean set(TInitiator initiator, Vector2 value) {
        return set(initiator, value.x, value.y);
    }

    public void get(Vector2 out) {
        out.set(vector);
    }

    public Vector2 ref() {
        return vector;
    }

    public static <T> Vec2Property<T> make(float x, float y) {
        return new Vec2Property<T>(x, y);
    }

    public static <T> Vec2Property<T> make(Vector2 value) {
        return new Vec2Property<T>(value);
    }

    public static <T> Vec2Property<T> make() {
        return new Vec2Property<T>();
    }
}
