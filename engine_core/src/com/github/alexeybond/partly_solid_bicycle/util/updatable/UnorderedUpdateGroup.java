package com.github.alexeybond.partly_solid_bicycle.util.updatable;

import com.badlogic.gdx.utils.Array;

/**
 *
 */
public class UnorderedUpdateGroup<T extends Updatable> implements UpdateGroup<T> {
    private final Array<T> items;

    public UnorderedUpdateGroup(int reserve) {
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
