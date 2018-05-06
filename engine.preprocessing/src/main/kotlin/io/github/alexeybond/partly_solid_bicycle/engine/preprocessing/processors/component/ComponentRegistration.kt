package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.component

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.mutation.Mutation
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.module.modifyModuleInit

class ComponentRegistration : ItemProcessor {
    override fun getPriority(): Int {
        return -1_000
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component" == itemKind
    }

    override fun processItem(context: ItemContext) {
        val modules: List<ItemContext> = context["modules"]
        val implClassName: ClassName = context["implClassName"]
        val envs: List<String> = context["envs"]
        val kind: String = context["componentKind"]
        val names: List<String> = context["names"]
        val isNodeType: Boolean = context["isNodeType"]

        val mut: Mutation<CodeBlock.Builder> = Mutation {
            val factoryST = if (isNodeType) "customNodeFactoryStrategy" else "componentNodeFactoryStrategy"

            names.forEach { name ->
                it.add("registrationStrategy.resolve($factoryST.resolve(\$T.getFactory(), \$T.getClassCompanionResolver()), \$S, \$S);\n",
                        implClassName, implClassName, name, kind)
            }
        }

        modules.forEach { module ->
            envs.forEach { env ->
                modifyModuleInit(module, env, mut)
            }
        }
    }
}
