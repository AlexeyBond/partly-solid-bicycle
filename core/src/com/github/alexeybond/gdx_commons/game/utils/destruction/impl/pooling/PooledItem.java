package com.github.alexeybond.gdx_commons.game.utils.destruction.impl.pooling;

public class PooledItem<TT extends PooledItem<TT>> {
    TT next = null;
    public boolean alive = false;
}
