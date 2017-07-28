package com.github.alexeybond.partly_solid_bicycle.ioc.ch;

import com.github.alexeybond.partly_solid_bicycle.ioc.IoCContext;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoCContextHolder;

/**
 *
 */
public class ThreadLocalContext implements IoCContextHolder {
    private static ThreadLocal<IoCContext> threadLocal = new ThreadLocal<IoCContext>();

    @Override
    public void set(IoCContext context) {
        threadLocal.set(context);
    }

    @Override
    public IoCContext get() {
        return threadLocal.get();
    }
}
