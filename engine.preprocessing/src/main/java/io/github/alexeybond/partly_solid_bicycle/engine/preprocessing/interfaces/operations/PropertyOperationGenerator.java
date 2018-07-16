package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.operations;

import com.squareup.javapoet.CodeBlock;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ProcessingContext;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.reflection.PropertyInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * <h1>Operations</h1>
 * <h2>"load"</h2>
 * <h3>Parameters</h3>
 * <ul>
 * <li>"type", TypeMirror - type of the writable variable</li>
 * <li>"lvalue", String - text of expression representing writable variable</li>
 * <li>"rvalue", String - text of expression representing the
 * {@link io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject
 * data object} containing serialized value of the variable</li>
 * </ul>
 * <h2>"load-property"</h2>
 * <h3>Parameters</h3>
 * <ul>
 * <li>"lvalue", String - text of expression representing the object containing the property</li>
 * <li>"rvalue", String - text of expression representing a
 * {@link io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject
 * data object} containing value for the property</li>
 * </ul>
 * <h2>"store"</h2>
 * <h3>Parameters</h3>
 * <ul>
 * <li>"type", TypeMirror - type of the variable</li>
 * <li>"lvalue", String - text of expression representing
 * {@link io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.OutputDataObject
 * data object} that should receive the value</li>
 * <li>"rvalue", String - text of expression representing value of the variable</li>
 * </ul>
 */
public interface PropertyOperationGenerator {
    /**
     * @param operation         the operation to generate code for
     * @param propertyInfo      information about the property related to the operation
     * @param processingContext context
     * @param root              root generator (to be used to generate nested operations)
     * @param args              arguments
     * @return the generated code
     */
    @Nullable
    CodeBlock generateOperation(
            @NotNull String operation,
            @NotNull PropertyInfo propertyInfo,
            @NotNull ProcessingContext processingContext,
            @NotNull PropertyOperationGenerator root,
            @NotNull Map<String, Object> args
    );
}
