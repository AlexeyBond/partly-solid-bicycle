package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;

public interface FieldLoadGenerator {
    @Nullable
    String generateRead(
            @NotNull ProcessingEnvironment processingEnvironment,
            @NotNull VariableElement field,
            @NotNull String lvalueExpr,
            @NotNull String rvalueExpr);
}
