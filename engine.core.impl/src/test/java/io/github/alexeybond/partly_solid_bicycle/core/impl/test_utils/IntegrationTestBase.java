package io.github.alexeybond.partly_solid_bicycle.core.impl.test_utils;

import io.github.alexeybond.partly_solid_bicycle.core.impl.app.module.ModuleSet;
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.module.ModuleSetBuilder;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.Module;
import org.junit.After;
import org.junit.Before;

public class IntegrationTestBase {
    private ModuleSetBuilder moduleSetBuilder = new ModuleSetBuilder();

    private ModuleSet moduleSet;

    protected void env(Object... envs) {
        for (Object env : envs) moduleSetBuilder = moduleSetBuilder.addEnv(env);
    }

    protected void modules(Module... modules) {
        for (Module module : modules) moduleSetBuilder = moduleSetBuilder.add(module);
    }

    @Before
    public void setUpEnvironment() {
        moduleSet = moduleSetBuilder.bootstrap();
    }

    @After
    public void shutdownEnvironment() {
        try {
            moduleSet.close();
        } finally {
            moduleSet = null;
        }
    }
}
