package io.github.alexeybond.partly_solid_bicycle.core.impl.app.module;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public enum ModuleUtils {
    ;

    public static Iterable<Iterable<String>> makeDependencyInfo(
            @NotNull Iterable<String> provided,
            @NotNull Iterable<String> directDependencies,
            @NotNull Iterable<String> reverseDependencies) {
        return Arrays.asList(provided, directDependencies, reverseDependencies);
    }
}
