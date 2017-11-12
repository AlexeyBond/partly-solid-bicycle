package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.FIELD,
        ElementType.TYPE,
        ElementType.METHOD,
})
public @interface Meta {
    String[] value();
}
