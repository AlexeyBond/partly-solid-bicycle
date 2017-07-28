package com.github.alexeybond.partly_solid_bicycle.ioc.ch;

import com.github.alexeybond.partly_solid_bicycle.ioc.IoCContext;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoCContextHolder;

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
