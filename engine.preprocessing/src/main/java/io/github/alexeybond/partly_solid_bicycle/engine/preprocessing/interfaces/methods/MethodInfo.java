package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.methods;

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.metadata.Metadata;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Element;
import java.util.List;

public interface MethodInfo {
    @NotNull
    Metadata getMetadata();

    @NotNull
    String getName();

    @NotNull
    List<Element> getDeclaringElements();
}
