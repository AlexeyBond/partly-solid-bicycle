package com.github.alexeybond.gdx_commons.util.parts;

/**
 * Abstract collection of named parts of some composite object.
 */
public interface AParts<TOwner, T extends Part<TOwner>> {
    void add(String name, T part);

    <TT extends T> TT get(String name);

    void remove(String name);

    TOwner owner();

    void removeAll();
}
