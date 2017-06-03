package com.github.alexeybond.gdx_commons.ioc.strategy;

import com.github.alexeybond.gdx_commons.ioc.IoCStrategy;

/**
 *
 */
public class Singleton implements IoCStrategy {
    private Object instance;

    public Singleton(Object instance) {
        this.instance = instance;
    }

    @Override
    public Object resolve(Object... args) {
        return instance;
    }
}
