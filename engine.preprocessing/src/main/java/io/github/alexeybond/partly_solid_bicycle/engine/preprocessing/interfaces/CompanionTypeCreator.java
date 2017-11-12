package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.TypeElement;

public interface CompanionTypeCreator {
    @NotNull
    Iterable<String> getCompanionTypes();

    TypeSpec generateCompanion(
            @NotNull String companionType,
            @NotNull ClassName className,
            @NotNull TypeElement componentClass);
}