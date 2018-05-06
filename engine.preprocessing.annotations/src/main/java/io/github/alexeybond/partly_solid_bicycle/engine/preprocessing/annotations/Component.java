package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for component classes.
 * <p>
 * Companion object classes and modules registering components and companions are generated for component classes.
 * </p>
 * <p>
 * Component class must have a default constructor.
 * </p>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Component {
    /**
     * @return names to register this component with
     */
    String[] name();

    /**
     * @return component kind
     */
    String kind();

    /**
     * @return environment types to register component within
     */
    String[] env() default {"default"};

    /**
     * @return modules this component should be registered by or empty array for default module
     */
    /*
     It's impossible to use <? extends Module> instead of just <?> as generated modules do not
     implement Module interface at the moment when the annotation is parsed :(
     */
    Class<?>[] modules() default {};

    Meta asMeta() default @Meta({
            "isComponent=true"
    });
}
