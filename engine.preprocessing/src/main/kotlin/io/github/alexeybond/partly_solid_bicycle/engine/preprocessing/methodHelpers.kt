package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.reflection.MethodInfo
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

class MethodInfoImpl(
        private val name: String,
        private val metadata: MetadataImpl
) : MethodInfo {
    private val declaringElements: MutableList<Element> = ArrayList()

    override fun getMetadata(): MetadataImpl {
        return metadata
    }

    override fun getName(): String {
        return name
    }

    override fun getDeclaringElements(): MutableList<Element> {
        return declaringElements
    }

    override fun getKind(): String {
        return "method"
    }

    internal fun addElement(element: Element) {
        declaringElements.add(element)
        metadata.addDataFrom(element)
    }
}

fun TypeElement.publicMethods(processingEnv: ProcessingEnvironment): Iterable<MethodInfo> {
    val methods: MutableMap<String, MethodInfoImpl> = HashMap()

    iterateMembers<ExecutableElement>(
            processingEnv,
            processingEnv.elementUtils.getTypeElement("java.lang.Object").asType(),
            { element -> element.kind === ElementKind.METHOD },
            { method ->
                val name = method.simpleName.toString()
                methods
                        .computeIfAbsent(
                                name,
                                { MethodInfoImpl(name, MetadataImpl(processingEnv, nullMetadata)) }
                        )
                        .addElement(method)
            }
    )

    return methods.values.filter { !it.metadata.isEmpty() }
}
