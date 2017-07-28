package com.github.alexeybond.partly_solid_bicycle.game.utils.destruction.impl.pooling;

public class PooledItem<TT extends PooledItem<TT>> {
    TT next = null;
    public boolean alive = false;
}
