package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.processors.component

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import javax.lang.model.type.TypeMirror

class ComponentTypeCheck : ItemProcessor {
    override fun getPriority(): Int {
        return Int.MIN_VALUE
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component" == itemKind
    }

    override fun processItem(context: ItemContext): Boolean {
        val pEnv = context.context.env
        val tm: TypeMirror = context["typeMirror"]

        val nodeTm = pEnv.elementUtils
                .getTypeElement(LogicNode::class.java.canonicalName)
                .asType()

        context["isNodeType"] = pEnv.typeUtils.isSubtype(tm, nodeTm)

        return false
    }
}
