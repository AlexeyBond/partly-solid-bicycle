package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * {@literal @}Component(/* ...{@literal *}/)
 * class Component {
 *     // self will point to the node this component is connected to
 *    {@literal @}From(".")
 *     public LogicNode self;
 *
 *     // public property initialized by loader
 *     public {@link io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNodePath LogicNodePath} refPath;
 *
 *     // property initialized by connector on connection event
 *    {@literal @}From("@refPath")
 *     public LogicNode ref;
 *
 *     // connector will automatically extract the component associated with node
 *     // if type of property is not LogicalNode
 *    {@literal @}From("../transform")
 *     public TransformComponent transform;
 *
 *     //
 *    {@literal @}From("@target|../target")
 *     public LogicNode target;
 * }
 * </pre>
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface From {
    String value();

    Meta asMeta() default @Meta({
            "property.bind=true",
            "property.bindExpression=$value"
    });
}
