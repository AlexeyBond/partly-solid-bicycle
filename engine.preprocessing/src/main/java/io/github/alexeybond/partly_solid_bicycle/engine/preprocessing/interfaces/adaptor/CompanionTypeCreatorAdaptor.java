package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.adaptor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.CompanionTypeCreator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Collections;

public class CompanionTypeCreatorAdaptor implements CompanionTypeCreator {
    private final static Iterable<String> DEFAULT_TYPES = Collections.emptyList();
    private final static Iterable<String> DEFAULT_ENVIRONMENTS = Collections.singleton("default");

    protected CompanionTypeCreatorAdaptor() {
    }

    @NotNull
    @Override
    public Iterable<String> getCompanionTypes() {
        return DEFAULT_TYPES;
    }

    @NotNull
    @Override
    public Iterable<String> getCompanionEnvironments() {
        return DEFAULT_ENVIRONMENTS;
    }

    @Nullable
    @Override
    public TypeSpec generateCompanion(
            @NotNull String environment,
            @NotNull ProcessingEnvironment processingEnvironment,
            @NotNull String companionType,
            @NotNull ClassName className,
            @NotNull TypeElement componentClass) {
        return null;
    }
}
