package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;

@Component(name = "component-6", kind = "any")
public class Component6 {
    private String a, b;
    public String content = "do not touch it!";

    public String getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public void setContent(String value) {
        a = value.substring(0, 1);
        b = value.substring(1);
    }
}
