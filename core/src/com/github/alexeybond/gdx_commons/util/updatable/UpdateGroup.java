package com.github.alexeybond.gdx_commons.util.updatable;

/**
 *
 */
public interface UpdateGroup<T extends Updatable> {
    void addItem(T item);

    void updateItems();
}
