package com.github.alexeybond.gdx_commons.ioc;

/**
 *
 */
public interface IoCContextHolder {
    void set(IoCContext context);

    IoCContext get();
}
