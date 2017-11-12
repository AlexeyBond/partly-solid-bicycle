package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotation_processors

import com.squareup.javapoet.ClassName
import javax.lang.model.element.TypeElement

fun companionClassName(componentClass: TypeElement, companionName: String): ClassName {
    val ccn = ClassName.get(componentClass)
    return ClassName.get(
            "generated.${ccn.packageName()}",
            "${ccn.simpleNames().joinToString(separator = "$")}_$companionName")
}
