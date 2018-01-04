package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotation_processors

import com.squareup.javapoet.ClassName
import com.sun.source.util.Trees
import com.sun.tools.javac.processing.JavacProcessingEnvironment
import com.sun.tools.javac.tree.JCTree
import com.sun.tools.javac.tree.TreeMaker
import com.sun.tools.javac.util.Names
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

/**
 * Name of static field of companion class containing a companion resolver.
 */
val COMPANION_RESOLVER_FIELD_NAME = "RESOLVER"

/**
 * Name of static field of component class containing a map from companion type name to companion resolver.
 */
val COMPANION_MAP_FIELD_NAME = "COMPANIONS"

fun companionClassName(componentClass: TypeElement, companionName: String): ClassName {
    val ccn = ClassName.get(componentClass)
    return ClassName.get(
            "generated.${ccn.packageName()}",
            "${ccn.simpleNames().joinToString(separator = "$")}_$companionName")
}

fun companionOwnerClassName(ccn: ClassName): ClassName {
    return ClassName.get(
            "generated.${ccn.packageName()}",
            "${ccn.simpleNames().joinToString(separator = "$")}$\$_companionOwner")
}

inline fun AnnotationMirror.getValue(eu: Elements, param: String): AnnotationValue {
    return eu.getElementValuesWithDefaults(this)
            .filterKeys { k -> k.simpleName.contentEquals(param) }
            .values.first()
}

inline fun <reified T> AnnotationValue.getListValue(): List<T> {
    return (value as List<*>).map { x -> (x as AnnotationValue).value as T }
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
    val selector = treeMaker.Ident(names.fromString(newBase.reflectionName()))

    classDecl.extending = selector
}
