package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class ModuleTest {
    @Test
    public void doTestInstantiation() {
        new Module1();
    }

    @Test
    public void doTestClassRebase() {
        Class<?> generatedModuleClass;
        try {
            generatedModuleClass = Module1.class.getClassLoader()
                    .loadClass("io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.GeneratedModule");
        } catch (ClassNotFoundException e) {
            // Old base class is not even loadable. It's fine.
            return;
        }

        // If old base class is loadable then check that it is no longer a base class of the module class
        assertFalse(generatedModuleClass.isAssignableFrom(Module1.class));
    }
}
