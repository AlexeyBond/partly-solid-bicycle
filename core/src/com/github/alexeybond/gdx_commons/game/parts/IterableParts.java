package com.github.alexeybond.gdx_commons.game.parts;

import com.badlogic.gdx.utils.Array;

import java.util.NoSuchElementException;

/**
 *
 */
public class IterableParts<TOwner, T extends Part<TOwner>> implements AParts<TOwner, T> {
    private static int RESERVE_SIZE = 16;

    private boolean isIterating = false;
    private final Array<T> iterationArray = new Array<T>(false, RESERVE_SIZE);
    private final Array<String> removeArray = new Array<String>(false, RESERVE_SIZE);

    private final Parts<TOwner, T> parent;

    public IterableParts(Parts<TOwner, T> parts) {
        this.parent = parts;
    }

    private void reallyRemove(String name) {
        try {
            T part = parent.get(name);
            iterationArray.removeValue(part, true);
            parent.remove(name);
        } catch (NoSuchElementException ignore) { }
    }

    private void flushRemovals() {
        for (int i = 0; i < removeArray.size; i++) {
            reallyRemove(removeArray.get(i));
        }

        removeArray.clear();
    }

    @Override
    public void add(String name, T part) {

    }

    @Override
    public <TT extends T> TT get(String name) {
        return parent.get(name);
    }

    @Override
    public void remove(String name) {
        if (isIterating) {
            removeArray.add(name);
        } else {
            reallyRemove(name);
        }
    }

    @Override
    public TOwner owner() {
        return parent.owner();
    }

    @Override
    public void removeAll() {
        parent.removeAll();
        iterationArray.clear();
    }

    public Array<T> startIterations() {
        isIterating = true;
        return iterationArray;
    }

    public void endIterations() {
        isIterating = false;
        flushRemovals();
    }
}
