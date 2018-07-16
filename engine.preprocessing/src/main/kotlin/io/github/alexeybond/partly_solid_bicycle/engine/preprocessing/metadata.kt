package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Meta
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.metadata.Metadata
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element

class MetadataImpl(
        processingEnv: ProcessingEnvironment,
        private val base: Metadata) : Metadata {
    private val tu = processingEnv.typeUtils
    private val eu = processingEnv.elementUtils

    private val map: MutableMap<String, String> = HashMap()

    override fun get(key: String): String? {
        return map[key] ?: base[key]
    }

    fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    fun addData(pair: String, referenceAnnotation: AnnotationMirror? = null) {
        val idx = pair.indexOf('=')
        if (idx < 0) {
            map[pair.trim()] = "true"
        } else {
            var value: String = pair.substring(idx + 1)

            referenceAnnotation?.let { ref ->
                if (value.startsWith("$")) {
                    val paramName = value.substring(1).trim()

                    value = referenceAnnotation
                            .getValue(eu, paramName)
                            .value.toString()
                }
            }

            map[pair.substring(0, idx).trim()] = value.trim()
        }
    }

    fun addDataFrom(annotation: Meta, referenceAnnotation: AnnotationMirror? = null) {
        annotation.value.forEach { addData(it, referenceAnnotation) }
    }

    fun addDataFrom(annotation: AnnotationMirror, referenceAnnotation: AnnotationMirror? = null) {
        if (tu.isSameType(
                        annotation.annotationType,
                        eu.getTypeElement(Meta::class.java.canonicalName).asType())) {
            annotation
                    .getValue(eu, "value")
                    .getListValue<String>()
                    .forEach { addData(it, referenceAnnotation) }
        }

        val metaRepr = try {
            annotation.getValue(eu, "asMeta").value as AnnotationMirror
        } catch (e: NoSuchElementException) {
            return
        }

        addDataFrom(metaRepr, annotation)
    }

    fun addDataFrom(element: Element) {
        element.annotationMirrors.forEach { addDataFrom(it) }
    }
}

val nullMetadata = Metadata { null }
