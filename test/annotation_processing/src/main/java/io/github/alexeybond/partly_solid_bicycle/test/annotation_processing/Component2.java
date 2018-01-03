package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Optional;

import java.util.List;

@Component({"component-2"})
public class Component2 {
    @Optional
    public List<List<String>> multilist;
}
