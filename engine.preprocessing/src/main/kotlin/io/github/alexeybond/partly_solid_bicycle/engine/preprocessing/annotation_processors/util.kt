package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotation_processors

import com.squareup.javapoet.ClassName
import com.sun.source.util.Trees
import com.sun.tools.javac.processing.JavacProcessingEnvironment
import com.sun.tools.javac.tree.JCTree
import com.sun.tools.javac.tree.TreeMaker
import com.sun.tools.javac.util.Names
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import kotlin.reflect.KClass

/**
 * Name of static field of companion class containing a companion resolver.
 */
val COMPANION_RESOLVER_FIELD_NAME = "RESOLVER"

/**
 * Name of static field of component class containing a instance of [MutableClassCompanionResolver]
 * for that class.
 */
val CLASS_COMPANION_RESOLVER_FIELD_NAME = "COMPANIONS"

/**
 * Name of static field of component class containing a instance of [GenericFactory] producing
 * instances of that class.
 */
val CLASS_FACTORY_FIELD_NAME = "FACTORY"

fun companionClassName(componentClass: TypeElement, companionName: String): ClassName {
    val ccn = ClassName.get(componentClass)
    return ClassName.get(
            "generated.${ccn.packageName()}",
            "${ccn.simpleNames().joinToString(separator = "$")}_$companionName")
}

fun companionOwnerClassName(ccn: ClassName): ClassName {
    return ClassName.get(
            "generated.${ccn.packageName()}",
            "${ccn.simpleNames().joinToString(separator = "$")}\$\$_companionOwner")
}

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

fun isSubclass(type: TypeElement, clazz: Class<*>, pe: ProcessingEnvironment): Boolean {
    return pe.typeUtils.isSubtype(
            type.asType(),
            pe.elementUtils.getTypeElement(clazz.canonicalName).asType());
}

/**
 * Checks if a type is subclass of [LogicNode].
 */
fun isNodeClass(type: TypeElement, pe: ProcessingEnvironment): Boolean {
    return isSubclass(type, LogicNode::class.java, pe)
}

fun reExtendClass(
        processingEnvironment: ProcessingEnvironment,
        clz: TypeElement,
        newBase: ClassName) {
    val trees = Trees.instance(processingEnvironment)
    val context = (processingEnvironment as JavacProcessingEnvironment).context
    val treeMaker = TreeMaker.instance(context)
    val names = Names.instance(context)

    val classDecl = trees.getTree(clz) as JCTree.JCClassDecl
    val selector = treeMaker.Select(
            treeMaker.Ident(names.fromString(newBase.packageName())),
            names.fromString(newBase.simpleNames().joinToString(separator = "$")))

    classDecl.extending = selector
}
