package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Optional;

@Component(name = "component-1", kind = "any")
public class Component1 {
    private String privateThing;

    public transient String transientValue;

    public final int INT = 41;

    public static int N;

    public float x, y, z;

    @Optional
    public String name = "bar";

    @Optional
    public InputDataObject data;
}
