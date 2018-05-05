package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.properties;

import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import java.util.List;

/**
 * Information about property of a class.
 */
public interface PropertyInfo {
    /**
     * @return name of the property
     */
    @NotNull
    String getName();

    @NotNull
    TypeMirror getType();

    boolean isReadable();

    boolean isWritable();

    boolean hasField();

    boolean hasSetter();

    boolean hasGetter();

    @NotNull
    String getSetterName();

    @NotNull
    String getGetterName();

    /**
     * Returns list of all elements associated with this property.
     * <p>
     * The returned list will include elements for setters, getters and fields including ones from
     * superclasses of the analyzed class.
     *
     * @return list of all elements associated with this property
     */
    @NotNull
    List<Element> getDeclaringElements();

    /**
     * Returns list of all annotations associated with this property.
     * <p>
     * Returned annotations include ones annotating getters, setters and fields associated with
     * this property.
     * Returned list may include multiple annotations of the same type.
     *
     * @return list of all annotations associated with this property
     */
    @NotNull
    List<AnnotationMirror> getAnnotations();
}
