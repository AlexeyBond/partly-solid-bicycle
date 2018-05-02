package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.processors.component

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.TypeSpec
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.add

class ImplCompanionResolverInit : ItemProcessor {
    override fun getPriority(): Int {
        return -1_700
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component" == itemKind
    }

    override fun processItem(context: ItemContext): Boolean {
        val resolverFieldName: String = context["companionResolverField"]
        val implMutations: Mutations<TypeSpec.Builder> = context["implMutations"]

        implMutations.add {
            // TODO: The next line of code is a VERY bad thing because of many reasons
            addStaticBlock(CodeBlock
                    .of("$resolverFieldName = new io.github.alexeybond.partly_solid_bicycle.core.impl.common.companions.MutableClassCompanionResolverImpl();"))
        }

        return false
    }
}
