package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.mutation.Mutation
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.module.modifyModuleInit

class CompanionRegistration : ItemProcessor {
    override fun getPriority(): Int {
        return 0
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component-companion" == itemKind
    }

    override fun processItem(context: ItemContext) {
        val componentCtx: ItemContext = context["component"]
        val envs: List<String> = context["envs"]
        val modules: List<ItemContext> = componentCtx["modules"]
        val componentImplCN: ClassName = componentCtx["implClassName"]
        val companionImplCN: ClassName = context["className"]
        val companionType: String = context["companionType"]

        val mut: Mutation<CodeBlock.Builder> = Mutation {
            it.add("\$T.getClassCompanionResolver().register(\$S, \$T.RESOLVER);\n",
                    componentImplCN, companionType, companionImplCN)
        }

        envs.forEach { env ->
            modules.forEach { module ->
                modifyModuleInit(module, env, mut)
            }
        }
    }
}
