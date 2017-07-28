package com.github.alexeybond.partly_solid_bicycle.util.parts;

import com.badlogic.gdx.Gdx;
import com.github.alexeybond.partly_solid_bicycle.util.parts.exceptions.PartConnectException;
import com.github.alexeybond.partly_solid_bicycle.util.parts.exceptions.PartConnectRejectedException;

import java.util.*;

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
    public <TT extends T> TT add(String name, TT part) {
        TT exist = (TT) parts.get(name);
        if (null != exist) return exist;
        parts.put(name, part);

        try {
            part.onConnect(owner);
        } catch (PartConnectRejectedException e) {
            parts.remove(name);
            Gdx.app.log("DEBUG",
                    "Part " + part + " refuses to connect to " + this + " as \"" + name + "\"", e);
            return null;
        } catch (Exception e) {
            parts.remove(name);
            throw new PartConnectException("Could not connect " + part + " as \"" + name + "\"", e);
        }

        return part;
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
        Iterator<Map.Entry<String, T>> iterator = parts.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, T> entry = iterator.next();
            entry.getValue().onDisconnect(owner);

            try {
                // Sometimes removal of some components causes recursive removal of others
                // and ConcurrentModificationException then.
                iterator.remove();
            } catch (ConcurrentModificationException e) {
                parts.remove(entry.getKey());
                iterator = parts.entrySet().iterator();
            }
        }
    }
}
