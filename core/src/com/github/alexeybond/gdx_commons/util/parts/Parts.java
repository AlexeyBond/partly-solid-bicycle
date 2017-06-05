package com.github.alexeybond.gdx_commons.util.parts;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 */
public class Parts<TOwner, T extends Part<TOwner>> implements AParts<TOwner, T> {
    private final TOwner owner;
    private final Map<String, T> parts = new HashMap<String, T>();

    public Parts(TOwner owner) {
        this.owner = owner;
    }

    @Override
    public void add(String name, T part) {
        if (parts.containsKey(name)) return;
        parts.put(name, part);
        part.onConnect(owner);
    }

    @Override
    public <TT extends T> TT get(String name) {
        T part = parts.get(name);

        if (null == part) {
            throw new NoSuchElementException(name);
        }

        return (TT) part;
    }

    @Override
    public void remove(String name) {
        T part = parts.get(name);

        if (null != part) {
            parts.remove(name);
            part.onDisconnect(owner);
        }
    }

    @Override
    public TOwner owner() {
        return owner;
    }

    @Override
    public void removeAll() {
        for (T part : parts.values()) {
            part.onDisconnect(owner);
        }

        parts.clear();
    }
}
