package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations;

public @interface SerializedName {
    String value();

    Meta asMeta() default @Meta({
            "property.serializedName=$value"
    });
}
