package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Makes connector companion assign a node at given path (or component attached to that node) to the property.
 *
 * <p>
 * Example:
 * </p>
 * <pre>
 * {@literal @}Component(/* ...{@literal *}/)
 *  class MyComponent {
 *      // This property will refer to the node this component is attached to
 *     {@literal @}FromPath(".")
 *      public LogicNode self;
 *
 *      // This property will refer to a component attached to a child node with id "transform"
 *      // of parent node of the node this component is attached to.
 *      // When type of property is not {@link
 *      io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode
 *      LogicNode} connector will try to cast
 *      // component attached to corresponding node to type of the property
 *     {@literal @}FromPath("../transform")
 *      public TransformComponent transform;
 *  }
 * </pre>
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface FromPath {
    String value();

    Meta asMeta() default @Meta({
            "property.bind=true",
            "property.bindMode=path",
            "property.bindPath=$value"
    });
}
