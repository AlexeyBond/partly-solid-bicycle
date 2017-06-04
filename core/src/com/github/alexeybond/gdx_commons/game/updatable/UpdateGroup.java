package com.github.alexeybond.gdx_commons.game.updatable;

/**
 *
 */
public interface UpdateGroup<T extends Updatable> {
    void addItem(T item);

    void updateItems();
}
