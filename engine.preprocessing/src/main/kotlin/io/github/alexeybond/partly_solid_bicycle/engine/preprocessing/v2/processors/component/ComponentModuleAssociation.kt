package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.processors.component

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotation_processors.getListValue
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotation_processors.getValue
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic

class ComponentModuleAssociation : ItemProcessor {
    override fun getPriority(): Int {
        return -2_000
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component" == itemKind
    }

    override fun processItem(context: ItemContext): Boolean {
        val pCtx = context.context
        val pEnv = pCtx.env
        val element: TypeElement = context["element"]
        val annotationMirror: AnnotationMirror = context["annotation"]

        val moduleClasses: List<TypeMirror> = annotationMirror
                .getValue(pEnv.elementUtils, "modules")
                .getListValue()

        val moduleItems: List<ItemContext> = if (moduleClasses.isEmpty()) {
            try {
                listOf(pCtx.getItem("module::default").get<ItemContext>("module"))
            } catch (e: NoSuchElementException) {
                pEnv.messager.printMessage(
                        Diagnostic.Kind.ERROR,
                        "No modules defined and there is no default module",
                        element, annotationMirror
                )
                return false
            }
        } else {
            moduleClasses.mapNotNull { classMirror ->
                val name = ((classMirror as DeclaredType).asElement() as TypeElement)
                        .qualifiedName.toString()
                try {
                    pCtx.getItem("module:$name")
                } catch (e: NoSuchElementException) {
                    pEnv.messager.printMessage(
                            Diagnostic.Kind.ERROR,
                            "$name is not a module class",
                            element, annotationMirror,
                            annotationMirror.getValue(pEnv.elementUtils, "modules")
                    )
                    null
                }
            }
        }

        context["modules"] = moduleItems

        return false
    }
}
