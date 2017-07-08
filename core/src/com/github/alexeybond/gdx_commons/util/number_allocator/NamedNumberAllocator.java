package com.github.alexeybond.gdx_commons.util.number_allocator;

import com.badlogic.gdx.utils.ObjectIntMap;

/**
 * A object returning a number for an input string. Always returns the same number for equal strings
 * and different numbers for non-equal strings.
 */
public abstract class NamedNumberAllocator {
    private ObjectIntMap<String> map = new ObjectIntMap<String>();
    private int next = first();

    public final int resolve(String name) {
        int present = map.get(name, unused());

        if (unused() == present) {
            int res = next;
            check(res);
            next = next(res);
            map.put(name, res);
            return res;
        } else {
            return present;
        }
    }

    /** A number that will never be returned by {@link #next(int)}. */
    protected abstract int unused();

    /** A number that will be returned for first string passed to {@link #resolve(String)}. */
    protected abstract int first();

    /** Generate next number. */
    protected abstract int next(int last);

    /** Check if given number is valid sequence number.
     *
     * @throws IllegalStateException if given value is not valid
     */
    protected abstract void check(int value);
}
