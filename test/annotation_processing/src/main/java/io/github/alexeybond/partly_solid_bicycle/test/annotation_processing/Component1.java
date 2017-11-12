package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;

@Component("component-1")
public class Component1 {
    private String privateThing;

    public transient String transientValue;

    public final int INT = 41;

    public static int N;

    public float x, y, z;

    public String name;
}
