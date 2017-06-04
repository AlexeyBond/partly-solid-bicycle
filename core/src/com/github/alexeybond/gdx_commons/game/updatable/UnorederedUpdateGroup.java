package com.github.alexeybond.gdx_commons.game.updatable;

import com.badlogic.gdx.utils.Array;

/**
 *
 */
public class UnorederedUpdateGroup<T extends Updatable> implements UpdateGroup<T> {
    private final Array<T> items;

    public UnorederedUpdateGroup(int reserve) {
        items = new Array<T>(false, reserve);
    }

    @Override
    public void addItem(T item) {
        items.add(item);
    }

    @Override
    public void updateItems() {
        for (int i = 0; i < items.size; i++) {
            T item = items.get(i);

            if (item.isAlive()) {
                item.update();
            } else {
                // Unordered array will move last element to position of removed element
                items.removeIndex(i);
                --i;
            }
        }
    }
}
