package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.component

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.TypeSpec
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor

val mutableClassCompanionResolverCN = ClassName.get(
        "io.github.alexeybond.partly_solid_bicycle.core.impl.common.companions",
        "MutableClassCompanionResolverImpl"
)

class ImplCompanionResolverInit : ItemProcessor {
    override fun getPriority(): Int {
        return -1_700
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component" == itemKind
    }

    override fun processItem(context: ItemContext) {
        val resolverFieldName: String = context["companionResolverField"]
        val implMutations: Mutations<TypeSpec.Builder> = context["implMutations"]

        implMutations.add {
            addStaticBlock(CodeBlock
                    .of("$resolverFieldName = new \$T();\n", mutableClassCompanionResolverCN))
        }
    }
}
