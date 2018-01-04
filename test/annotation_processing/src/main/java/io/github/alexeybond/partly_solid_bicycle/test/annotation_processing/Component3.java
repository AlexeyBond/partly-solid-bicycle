package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;

@Component(name = "component-3", kind = "any")
public class Component3 {
    public float[] floats;

    public int[][][] ints;
}
