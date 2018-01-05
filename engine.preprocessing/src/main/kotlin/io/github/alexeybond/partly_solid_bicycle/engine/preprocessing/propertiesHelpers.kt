package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing

import java.util.regex.Pattern
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.*
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic
import kotlin.reflect.KClass

class TypeProperty(
        val name: String,
        val type: TypeMirror
) {
    var isReadable = false
        private set
    var isWritable = false
        private set
    var hasSetter = false
        private set
    var hasGetter = false
        private set
    var hasField = false
        private set
    val annotations: MutableList<AnnotationMirror> = ArrayList()
    var declaringElements: MutableList<Element> = ArrayList()

    var getterName: String? = null
        private set
    var setterName: String? = null
        private set
    var fieldName: String? = null
        private set

    private fun addElement(element: Element) {
        declaringElements.add(element)
        annotations.addAll(element.annotationMirrors)
    }

    internal fun addField(element: VariableElement) {
        isReadable = true
        isWritable = true
        hasField = true
        fieldName = element.simpleName.toString()
        addElement(element)
    }

    internal fun addSetter(element: ExecutableElement) {
        hasSetter = true
        isWritable = true
        setterName = element.simpleName.toString()
        addElement(element)
    }

    internal fun addGetter(element: ExecutableElement) {
        hasGetter = true
        isReadable = true
        getterName = element.simpleName.toString()
        addElement(element)
    }

    internal fun verifyType(processingEnv: ProcessingEnvironment, type: TypeMirror, declaringElement: Element) {
        if (processingEnv.typeUtils.isSameType(type, this.type)) return

        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                "Type of property '$name' does not match the previously declared one.",
                declaringElement)
        declaringElements.forEach({ elem ->
            processingEnv.messager.printMessage(Diagnostic.Kind.NOTE,
                    "Property '$name' previously was declared here:",
                    elem)
        })
    }
}

val GETTER_PATTERN = Pattern.compile("^get([A-Z])(.*)$")!!
val SETTER_PATTERN = Pattern.compile("^set([A-Z])(.*)$")!!

fun TypeElement.publicPropertirs(
        processingEnv: ProcessingEnvironment
): Iterable<TypeProperty> {
    val variableStopModifiers = setOf(
            Modifier.PRIVATE, Modifier.PROTECTED, Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
    val methodStopModifiers = setOf(Modifier.PRIVATE, Modifier.PROTECTED, Modifier.STATIC)
    val objectClass = processingEnv.elementUtils.getTypeElement("java.lang.Object").asType()

    val properties = HashMap<String, TypeProperty>()

    this.iterateMembers<VariableElement>(
            processingEnv, objectClass,
            { element ->
                element.kind == javax.lang.model.element.ElementKind.FIELD &&
                        (element as VariableElement).modifiers.intersect(variableStopModifiers).isEmpty()
            },
            { variable ->
                val name = variable.simpleName.toString()
                val type = variable.asType()

                val prop = properties.computeIfAbsent(name, { TypeProperty(name, type) })
                prop.addField(variable)
            }
    )

    this.iterateMembers<ExecutableElement>(
            processingEnv, objectClass,
            { element ->
                element.kind == javax.lang.model.element.ElementKind.METHOD &&
                        (element as ExecutableElement).modifiers.intersect(methodStopModifiers).isEmpty()
            },
            l@ { method ->
                val getMatch = GETTER_PATTERN.matcher(method.simpleName)
                if (getMatch.matches()) {
                    val name = getMatch.group(1).toLowerCase() + getMatch.group(2)
                    val type = method.returnType

                    if (method.parameters.size != 0) {
                        processingEnv.messager.printMessage(javax.tools.Diagnostic.Kind.WARNING,
                                "Method seems to be a getter for property '$name' " +
                                        "but has parameters.",
                                method)
                        return@l
                    }

                    val prop = properties.computeIfAbsent(name, { TypeProperty(name, type) })
                    prop.addGetter(method)
                }

                val setMatch = SETTER_PATTERN.matcher(method.simpleName)
                if (setMatch.matches()) {
                    val name = setMatch.group(1).toLowerCase() + setMatch.group(2)

                    val parameters = method.parameters

                    if (parameters.size != 1) {
                        processingEnv.messager.printMessage(javax.tools.Diagnostic.Kind.WARNING,
                                "Method seems to be a setter for property '$name' " +
                                        "but has ${parameters.size} parameters.",
                                method)
                        return@l
                    }

                    val type = parameters[0].asType()


                    val prop = properties.computeIfAbsent(name, { TypeProperty(name, type) })
                    prop.addSetter(method)
                }
            }
    )

    return properties.values
}

fun <T : Element> TypeElement.iterateMembers(
        processingEnv: ProcessingEnvironment,
        stopClass: TypeMirror,
        filter: (Element) -> Boolean,
        action: (T) -> Unit
) {
    if (this.superclass.kind != TypeKind.NONE && !processingEnv.typeUtils.isSameType(stopClass, this.superclass)) {
        val superclassElement = ((this.superclass as DeclaredType).asElement() as TypeElement)
        superclassElement.iterateMembers<T>(processingEnv, stopClass, filter, action)
    }

    this.enclosedElements
            .filter(filter)
            .forEach({ e -> action(e as T) })
}

fun <T : Annotation> TypeProperty.getAnnotationMirror(
        processingEnv: ProcessingEnvironment, clz: KClass<T>): AnnotationMirror? {
    val tu = processingEnv.typeUtils
    val type = processingEnv.elementUtils.getTypeElement(clz.java.canonicalName).asType()
    return this.annotations
            .find { a -> tu.isSameType(a.annotationType, type) }
}

