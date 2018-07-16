package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.SerializedName;

@Component(name = "component-11", kind = "any")
public class Component11 {
    @SerializedName("\"--the-value--")
    public String value;
}
