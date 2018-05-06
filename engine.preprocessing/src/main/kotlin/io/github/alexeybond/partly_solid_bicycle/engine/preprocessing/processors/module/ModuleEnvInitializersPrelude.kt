package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.module

import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor

class ModuleEnvInitializersPrelude : ItemProcessor {
    override fun getPriority(): Int {
        return 1_000
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "module" == itemKind
    }

    override fun processItem(context: ItemContext) {
        val preludeMutations: Mutations<CodeBlock.Builder> = context["envInitializerPreludeMutations"]

        preludeMutations.add {
            add("\$T componentNodeFactoryStrategy = \$T.resolveStrategy(\$S);\n",
                    IoCStrategy::class.java, IoC::class.java,
                    "create component node factory")
            add("\$T customNodeFactoryStrategy = \$T.resolveStrategy(\$S);\n",
                    IoCStrategy::class.java, IoC::class.java,
                    "create custom node factory")
            add("\$T registrationStrategy = \$T.resolveStrategy(\$S);\n",
                    IoCStrategy::class.java, IoC::class.java,
                    "register node factory")
        }
    }
}
