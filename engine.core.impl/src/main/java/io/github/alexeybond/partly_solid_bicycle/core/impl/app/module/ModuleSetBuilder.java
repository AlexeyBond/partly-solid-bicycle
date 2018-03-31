package io.github.alexeybond.partly_solid_bicycle.core.impl.app.module;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.Module;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleSetBuilder {
    private final List<Module> modules = new ArrayList<Module>();
    private final List<Object> environments = new ArrayList<Object>(Collections.singleton("default"));

    public ModuleSetBuilder add(@NotNull Module module) {
        modules.add(module);

        return this;
    }

    public ModuleSetBuilder addEnv(@NotNull Object env) {
        environments.add(env);

        return this;
    }

    public ModuleSet bootstrap() {
        return new ModuleSet(modules, environments);
    }
}
