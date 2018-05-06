package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element
import javax.lang.model.util.Elements
import kotlin.reflect.KClass

internal fun Element.getAnnotationMirror(
        pEnv: ProcessingEnvironment,
        clz: KClass<out Annotation>): AnnotationMirror? {
    val t = pEnv.elementUtils.getTypeElement(clz.java.canonicalName).asType()
    return this.annotationMirrors
            .find { m -> pEnv.typeUtils.isSameType(m.annotationType, t) }
}

internal fun AnnotationMirror.getValue(eu: Elements, param: String): AnnotationValue {
    return eu.getElementValuesWithDefaults(this)
            .filterKeys { k -> k.simpleName.contentEquals(param) }
            .values.first()
}

inline fun <reified T> AnnotationValue.getListValue(): List<T> {
    return (value as List<*>).map { x -> (x as AnnotationValue).value as T }
}
