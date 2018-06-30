package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector

import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeAttachmentListener
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.LinearOrder
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.isSubclass
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

class AttachmentListenerConnect : ItemProcessor {
    val ORDER = LinearOrder(0)

    override fun getPriority(): Int {
        return 0
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component-companion" == itemKind
    }

    override fun processItem(context: ItemContext) {
        if (context.get<String>("companionType") != "connector") return

        val componentCtx: ItemContext = context["component"]

        val componentTM: TypeMirror = componentCtx["typeMirror"]
        val componentElem: TypeElement = componentCtx["element"]

        if (!isSubclass(componentElem, NodeAttachmentListener::class.java, componentCtx.context.env))
            return

        val connectMut: Mutations<CodeBlock.Builder> =
                context["codeMutations:method:onConnected"]
        val disconnectMut: Mutations<CodeBlock.Builder> =
                context["codeMutations:method:onDisconnected"]

        connectMut.add(ORDER) {
            add("component.onAttached(node);\n")
        }

        disconnectMut.add(ORDER.opposite) {
            add("component.onDetached(node);\n")
        }
    }
}
