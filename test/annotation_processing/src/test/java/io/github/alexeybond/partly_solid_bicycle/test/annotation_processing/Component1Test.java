package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import generated.io.github.alexeybond.partly_solid_bicycle.test.annotation_processing.Component1_loader;
import io.github.alexeybond.partly_solid_bicycle.core.impl.data.dynamic.DynamicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import org.junit.Test;

public class Component1Test {
    @Test public void doTest() {
        InputDataObject dataObject = new DynamicNode();

        Component1 component = new Component1();

        new Component1_loader().load(component, dataObject);
    }
}
