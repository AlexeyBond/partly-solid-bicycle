package com.github.alexeybond.gdx_commons.game.utils.destruction.impl.pooling;

/**
 *
 */
public abstract class LinkedPool<T extends PooledItem<T>> {
    private T firstFree, firstActive;
    private int allocTotal = 0;

    protected LinkedPool(int preAlloc) {
        for (int i = 0; i < preAlloc; i++) {
            T item = alloc();
            item.next = firstFree;
            firstFree = item;
            ++allocTotal;
        }
    }

    protected abstract T alloc();

    public T acquire() {
        T item = firstFree;
        if (item != null) {
            firstFree = item.next;
        } else {
            item = alloc();
            ++allocTotal;
        }

        item.next = firstActive;
        firstActive = item;
        item.alive = true;

        return item;
    }

    public void releaseAll() {
        T item = firstActive, next;

        while (null != item) {
            next = item.next;
            item.alive = false;
            item.next = firstFree;
            firstFree = item;
            item = next;
        }

        firstActive = null;
    }

    private T next0(T item, T prev) {
        while (null != item && !item.alive) {
            T na = item.next;
            item.next = firstFree;
            firstFree = item;

            if (firstActive == item) firstActive = na;

            item = na;
        }

        if (null != prev) prev.next = item;
        return item;
    }

    public T first() {
        return next0(firstActive, null);
    }

    public T next(T item) {
        return next0(item.next, item);
    }
}
