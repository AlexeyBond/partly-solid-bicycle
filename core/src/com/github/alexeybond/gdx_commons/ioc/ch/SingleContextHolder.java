package com.github.alexeybond.gdx_commons.ioc.ch;

import com.github.alexeybond.gdx_commons.ioc.IoCContext;
import com.github.alexeybond.gdx_commons.ioc.IoCContextHolder;

/**
 *
 */
public class SingleContextHolder implements IoCContextHolder {
    private IoCContext context;

    @Override
    public void set(IoCContext context) {
        this.context = context;
    }

    @Override
    public IoCContext get() {
        return context;
    }
}
