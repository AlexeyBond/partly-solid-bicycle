package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.mutation.MutationAccumulator;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * Context of annotation processing process.
 */
public interface ProcessingContext {
    /**
     * @return the {@link ProcessingEnvironment} provided by compiler
     */
    @NotNull
    ProcessingEnvironment getEnv();

    /**
     * Add a generated class
     *
     * @param className name of the class
     * @param builder   class builder
     * @return {@link MutationAccumulator} instance to receive mutations for the class builder
     */
    @NotNull
    MutationAccumulator<TypeSpec.Builder> addGenerated(
            @NotNull ClassName className, TypeSpec.Builder builder);

    /**
     * Execute all possible actions on given {@link ItemContext}.
     *
     * @param item the item to process
     */
    void processItem(@NotNull ItemContext item);

    @NotNull
    ItemContext newItem(@NotNull String id);
}
