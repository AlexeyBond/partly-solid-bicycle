package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.exceptions.NoLoadRequiredException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

/**
 * Object that generates Java code that loads value of a field/variable from a {@link InputDataObject}.
 */
public interface FieldLoadGenerator {
    /**
     * Generate the code.
     * <p>
     * <p>
     * For example when {@code lvalueExpr} is {@code "this.name"}, {@code rvalueExpr} is {@code "data"}
     * and {@code targetType} is mirror for {@link String} some implementation may return the following
     * code:
     * <pre>
     *   this.name = data.getString();
     *  </pre>
     * </p>
     *
     * @param processingEnvironment processing environment
     * @param targetType            type of the variable that should be assigned
     * @param lvalueExpr            text representation of expression that evaluates to a reference to the
     *                              variable that should be assigned
     * @param rvalueExpr            text representation of expression that evaluates to a {@link InputDataObject}
     *                              containing value that should be assigned
     * @return the code that assigns value of {@code rvalueExpr} to {@code lvalueExpr}
     * @throws NoLoadRequiredException if no assignment should be generated for variable of given type
     */
    @Nullable
    String generateRead(
            @NotNull ProcessingEnvironment processingEnvironment,
            @NotNull TypeMirror targetType,
            @NotNull String lvalueExpr,
            @NotNull String rvalueExpr)
            throws NoLoadRequiredException;
}
