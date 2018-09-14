package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.metadata.Metadata
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.reflection.PropertyInfo
import java.util.regex.Pattern
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.*
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic
import kotlin.reflect.KClass

class TypeProperty(
        private val name: String,
        private val type: TypeMirror,
        private val metadataImpl: MetadataImpl
) : PropertyInfo {
    override fun getGetterName(): String {
        return getterName ?: throw IllegalStateException("No getter")
    }

    override fun getSetterName(): String {
        return setterName ?: throw IllegalStateException("No setter")
    }

    override fun getName(): String {
        return name
    }

    override fun getType(): TypeMirror {
        return type
    }

    override fun isReadable(): Boolean {
        return readable
    }

    override fun isWritable(): Boolean {
        return writable
    }

    override fun hasField(): Boolean {
        return hasField
    }

    override fun hasSetter(): Boolean {
        return hasSetter
    }

    override fun hasGetter(): Boolean {
        return hasGetter
    }

    override fun getDeclaringElements(): List<Element> {
        return declaringElements
    }

    override fun getAnnotations(): List<AnnotationMirror> {
        return annotations
    }

    override fun getKind(): String {
        return "property"
    }

    override fun getMetadata(): Metadata {
        return metadataImpl
    }

    private var readable = false
    private var writable = false
    private var hasSetter = false
    private var hasGetter = false
    private var hasField = false
    private val annotations: MutableList<AnnotationMirror> = ArrayList()
    private var declaringElements: MutableList<Element> = ArrayList()

    private var getterName: String? = null
    private var setterName: String? = null
    private var fieldName: String? = null

    internal fun addElement(element: Element) {
        declaringElements.add(element)
        annotations.addAll(element.annotationMirrors)
        metadataImpl.addDataFrom(element)
    }

    internal fun addField(element: VariableElement) {
        readable = true
        writable = true
        hasField = true
        fieldName = element.simpleName.toString()
        addElement(element)
    }

    internal fun addSetter(element: ExecutableElement) {
        hasSetter = true
        writable = true
        setterName = element.simpleName.toString()
        addElement(element)
    }

    internal fun addGetter(element: ExecutableElement) {
        hasGetter = true
        readable = true
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

val STOP_CLASS_NAMES = setOf<String>(
        java.lang.Object::class.java.canonicalName,
        LogicNode::class.java.canonicalName
)

fun TypeElement.publicPropertirs(
        processingEnv: ProcessingEnvironment
): Iterable<TypeProperty> {
    val variableStopModifiers = setOf(
            Modifier.PRIVATE, Modifier.PROTECTED, Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
    val methodStopModifiers = setOf(Modifier.PRIVATE, Modifier.PROTECTED, Modifier.STATIC)
    val objectClass = processingEnv.elementUtils.getTypeElement("java.lang.Object").asType()

    val properties = HashMap<String, TypeProperty>()

    fun addProperty(name: String, type: TypeMirror): TypeProperty {
        return properties.computeIfAbsent(name, {
            TypeProperty(name, type, MetadataImpl(processingEnv, nullMetadata))
        })
    }

    this.iterateMembers<VariableElement>(
            processingEnv, objectClass,
            { element ->
                element.kind == javax.lang.model.element.ElementKind.FIELD &&
                        (element as VariableElement).modifiers.intersect(variableStopModifiers).isEmpty()
            },
            { variable ->
                val name = variable.simpleName.toString()
                val type = variable.asType()

                addProperty(name, type).addField(variable)
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

                    addProperty(name, type).addGetter(method)
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

                    addProperty(name, type).addSetter(method)
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
    if (this.qualifiedName.toString() in STOP_CLASS_NAMES)
        return

    if (this.superclass.kind != TypeKind.NONE && !processingEnv.typeUtils.isSameType(stopClass, this.superclass)) {
        val superclassElement = ((this.superclass as DeclaredType).asElement() as TypeElement)
        superclassElement.iterateMembers<T>(processingEnv, stopClass, filter, action)
    }

    this.enclosedElements
            .filter(filter)
            .forEach({ e -> action(e as T) })
}

class SyntheticPropertyInfo(
        private val name: String,
        private val type: TypeMirror,
        private val readable: Boolean,
        private val writable: Boolean,
        private val getter: String?,
        private val setter: String?,
        private val hasField: Boolean,
        private val declaringElements: List<Element>,
        private val metadata: Metadata
) : PropertyInfo {
    override fun getKind(): String {
        return "property"
    }

    override fun getName(): String {
        return name
    }

    override fun getType(): TypeMirror {
        return type
    }

    override fun isReadable(): Boolean {
        return readable
    }

    override fun isWritable(): Boolean {
        return writable
    }

    override fun hasField(): Boolean {
        return hasField
    }

    override fun hasSetter(): Boolean {
        return setter != null
    }

    override fun hasGetter(): Boolean {
        return getter != null
    }

    override fun getSetterName(): String {
        return setter ?: throw IllegalStateException("No setter")
    }

    override fun getGetterName(): String {
        return setter ?: throw IllegalStateException("No setter")
    }

    override fun getDeclaringElements(): List<Element> {
        return declaringElements
    }

    override fun getAnnotations(): List<AnnotationMirror> {
        return listOf()
    }

    override fun getMetadata(): Metadata {
        return metadata
    }
}

fun <T : Annotation> PropertyInfo.getAnnotationMirror(
        processingEnv: ProcessingEnvironment, clz: KClass<T>): AnnotationMirror? {
    val tu = processingEnv.typeUtils
    val type = processingEnv.elementUtils.getTypeElement(clz.java.canonicalName).asType()
    return this.annotations
            .find { a -> tu.isSameType(a.annotationType, type) }
}

fun PropertyInfo.generateAssignment(dstExpr: String, sourceExpr: String): String {
    return if (hasSetter()) {
        "$dstExpr.$setterName($sourceExpr);"
    } else {
        "$dstExpr.$name = $sourceExpr;"
    }
}

fun PropertyInfo.generateRead(srcExpr: String): String {
    return if (hasGetter()) {
        "$srcExpr.$getterName()"
    } else {
        "$srcExpr.$name"
    }
}

fun PropertyInfo.serializedName(): String {
    return metadata["property.serializedName"] ?: name
}