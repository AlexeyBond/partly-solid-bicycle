package com.github.alexeybond.gdx_commons.ioc.ch;

import com.github.alexeybond.gdx_commons.ioc.IoCContext;
import com.github.alexeybond.gdx_commons.ioc.IoCContextHolder;

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
