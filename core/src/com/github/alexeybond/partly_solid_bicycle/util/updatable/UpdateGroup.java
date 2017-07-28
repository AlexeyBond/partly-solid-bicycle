package com.github.alexeybond.partly_solid_bicycle.util.updatable;

/**
 *
 */
public interface UpdateGroup<T extends Updatable> {
    void addItem(T item);

    void updateItems();
}
