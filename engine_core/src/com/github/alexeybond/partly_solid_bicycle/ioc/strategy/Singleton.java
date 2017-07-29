package com.github.alexeybond.partly_solid_bicycle.ioc.strategy;

import com.github.alexeybond.partly_solid_bicycle.ioc.IoCStrategy;

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
