package com.github.alexeybond.gdx_commons.ioc.modules;

/**
 * Object that registers something in IoC container and/or initializes something.
 */
public interface Module {
    void init();

    void shutdown();
}
