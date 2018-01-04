package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionResolver;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.impl.SingletonCompanionResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for component companions implemented manually.
 * <p>
 * Annotated classes should contain a public static final field of type {@link CompanionResolver}
 * named {@code RESOLVER} and containing a {@link CompanionResolver} instance that will resolve
 * a companion instance for a component.
 * <pre>
 * {@link ComponentCompanion @ComponentCompanion}(component=MyComponent.class, companionType="cube")
 * public class CompanionCube implements ACompanion {
 *     public static final {@link CompanionResolver}<MyComponent, ACompanion> RESOLVER
 *         = new {@link SingletonCompanionResolver}(new CompanionCube());
 *
 *     {@link Override @Override}
 *     public void talk() {
 *         // ...
 *     }
 *
 *     // ...
 * }
 * </pre>
 * </p>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ComponentCompanion {
    /**
     * @return component class
     */
    Class<?> component();

    /**
     * @return companion type name
     */
    String companionType();

    String[] env() default {"default"};
}
