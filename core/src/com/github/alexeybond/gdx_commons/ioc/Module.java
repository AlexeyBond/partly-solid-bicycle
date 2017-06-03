package com.github.alexeybond.gdx_commons.ioc;

/**
 * Object that registers something in IoC container and/or initializes something.
 */
public interface Module {
    void init();

    void shutdown();
}
