package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.processors.component

import com.squareup.javapoet.ClassName
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.publicPropertirs
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.process
import javax.lang.model.element.TypeElement

class ComponentPropertyEnumeration : ItemProcessor {
    override fun getPriority(): Int {
        return 0
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component" == itemKind
    }

    override fun processItem(context: ItemContext): Boolean {
        val pCtx = context.context
        val componentType: TypeElement = context["element"]
        val className: ClassName = context["className"]

        componentType.publicPropertirs(pCtx.env).forEach({ prop ->
            pCtx.process("component-property:$className:${prop.name}") {
                set("kind", "component-property")
                set("info", prop)
                set("componentContext", context)
            }
        })

        return false
    }
}
