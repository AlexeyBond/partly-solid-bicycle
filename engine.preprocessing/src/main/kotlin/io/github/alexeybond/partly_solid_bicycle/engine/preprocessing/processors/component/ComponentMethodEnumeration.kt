package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.component

import com.squareup.javapoet.ClassName
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.process
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.publicMethods
import javax.lang.model.element.TypeElement

class ComponentMethodEnumeration : ItemProcessor {
    override fun getPriority(): Int {
        return 0
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component" == itemKind
    }

    override fun processItem(context: ItemContext) {
        val pCtx = context.context
        val componentType: TypeElement = context["element"]
        val className: ClassName = context["className"]

        componentType.publicMethods(pCtx.env).forEach({ method ->
            pCtx.process("component-method:$className:${method.name}") {
                set("kind", "component-method")
                set("info", method)
                set("componentContext", context)
            }
        })
    }
}
