package com.github.alexeybond.partly_solid_bicycle.ioc;

/**
 *
 */
public interface IoCContextHolder {
    void set(IoCContext context);

    IoCContext get();
}
