package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.saver

import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor

class SaveComponentClass : ItemProcessor {
    override fun getPriority(): Int {
        return 0
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return itemKind == "component-companion"
    }

    override fun processItem(context: ItemContext) {
        if (context.get<String>("companionType") != "saver") return

        val componentCtx: ItemContext = context["component"]
        val saveMutations: Mutations<CodeBlock.Builder> = context["codeMutations:method:save"]
        val componentNames: List<String> = componentCtx["names"]

        saveMutations.add {
            add("data.addField(\"class\").setString(\$S);\n", componentNames.first())
        }
    }
}
