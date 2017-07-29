package com.github.alexeybond.partly_solid_bicycle.ioc.modules;

/**
 * Object that registers something in IoC container and/or initializes something.
 */
public interface Module {
    void init();

    void shutdown();
}
