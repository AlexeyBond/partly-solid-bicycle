package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface OnEventAtPath {
    String value();

    Meta asMeta() default @Meta({
            "method.subscribe=true",
            "method.subscribeMode=path",
            "method.subscribePath=$value"
    });
}
