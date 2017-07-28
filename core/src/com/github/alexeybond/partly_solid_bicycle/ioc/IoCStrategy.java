package com.github.alexeybond.partly_solid_bicycle.ioc;

/**
 *
 */
public interface IoCStrategy {
    Object resolve(Object... args);
}
