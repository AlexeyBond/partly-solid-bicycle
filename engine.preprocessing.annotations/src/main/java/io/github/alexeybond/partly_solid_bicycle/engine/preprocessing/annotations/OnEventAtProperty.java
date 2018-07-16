package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Makes connector companion subscribe a method to events happening at a {@link
 * io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic Topic}
 * referred by a property of a component.
 * <p>
 * Note: connector will use value of the property at the moment of connection so:
 * <ul>
 * <li>property must be initialized before/on connection phase (using From*
 * annotations or in constructor/initializer)</li>
 * <li>changes to property will not affect the subscription</li>
 * </ul>
 * </p>
 * <p>
 * Example:
 * </p>
 * <pre>
 * {@literal @}Component(/* ...{@literal *}/)
 *  class MyComponent {
 *     {@literal @}FromPath("../transform")
 *      public Variable{@literal <}Transform> transform;
 *
 *      // Will be called every time the variable referred by "transform" property changes
 *     {@literal @}OnEventAtProperty("transform")
 *      public void onTransformChange() {
 *          /* ...{@literal *}/
 *      }
 *  }
 * </pre>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface OnEventAtProperty {
    String value();

    Meta asMeta() default @Meta({
            "method.subscribe=true",
            "method.subscribeMode=property",
            "method.subscribeProperty=$value"
    });
}
