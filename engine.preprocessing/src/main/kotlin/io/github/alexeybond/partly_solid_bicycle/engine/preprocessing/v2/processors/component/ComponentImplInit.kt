package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.processors.component

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeSpec
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.add
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

class ComponentImplInit : ItemProcessor {
    override fun getPriority(): Int {
        return -2_000
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component" == itemKind
    }

    override fun processItem(context: ItemContext): Boolean {
        val pCtx = context.context
        val componentCN: ClassName = context["className"]
        val componentElem: TypeElement = context["element"]

        if (componentElem.modifiers.contains(Modifier.FINAL)) {
            pCtx.env.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Component class should not be final",
                    componentElem
            )
            return true
        }

        val implCN = implementationClassName(componentCN)

        val implClassMutations = pCtx.addGenerated(
                implCN,
                TypeSpec.classBuilder(implCN)
        )

        implClassMutations.add {
            addModifiers(Modifier.PUBLIC)
            superclass(componentCN)
        }

        context["implClassName"] = implCN
        context["implMutations"] = implClassMutations

        return false
    }

    private fun implementationClassName(componentCN: ClassName): ClassName {
        return ClassName.get(
                "generated.${componentCN.packageName()}",
                "${componentCN.simpleNames().joinToString(separator = "$")}\$_impl"
        )
    }
}
