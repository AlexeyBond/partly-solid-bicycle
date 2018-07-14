package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.reflection;

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.metadata.Metadata;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Element;
import java.util.List;

/**
 * Information about some program element/group of elements (such as properties or methods)
 * with additional metadata.
 */
public interface ReflectionInfo {
    @NotNull
    Metadata getMetadata();

    @NotNull
    String getName();

    @NotNull
    List<Element> getDeclaringElements();

    @NotNull
    String getKind();
}
