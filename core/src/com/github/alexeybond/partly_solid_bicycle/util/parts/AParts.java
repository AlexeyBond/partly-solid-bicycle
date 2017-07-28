package com.github.alexeybond.partly_solid_bicycle.util.parts;

/**
 * Abstract collection of named parts of some composite object.
 */
public interface AParts<TOwner, T extends Part<TOwner>> {
    <TT extends T> TT add(String name, TT part);

    <TT extends T> TT get(String name);

    void remove(String name);

    TOwner owner();

    void removeAll();
}
