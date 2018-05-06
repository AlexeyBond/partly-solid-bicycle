package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.module

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeSpec
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.MutationAccumulatorImpl
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import org.jetbrains.annotations.NotNull
import javax.lang.model.element.Modifier

class ModuleEnvInitializersInit : ItemProcessor {
    // Map<Object, Runnable>
    val MAP_TN = ParameterizedTypeName.get(Map::class.java, Any::class.java, Runnable::class.java)

    override fun getPriority(): Int {
        return 0
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "module" == itemKind
    }

    override fun processItem(context: ItemContext): Boolean {
        val initMutations: Mutations<CodeBlock.Builder> = context["codeMutations:method:init"]
        val implMutations: Mutations<TypeSpec.Builder> = context["implMutations"]

        initMutations.add {
            beginControlFlow("for (\$T env : envs)",
                    Any::class.java)
            add("\$T initializer = ${ENV_INITIALIZERS_FIELD}.get(env);\n",
                    Runnable::class.java)
            beginControlFlow("if (null != initializer)")
            add("initializer.run();\n")
            endControlFlow()
            endControlFlow()
        }

        implMutations.add {
            addField(FieldSpec
                    .builder(MAP_TN, ENV_INITIALIZERS_FIELD,
                            Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .addAnnotation(NotNull::class.java)
                    .initializer("new \$T<\$T, \$T>()",
                            HashMap::class.java, Any::class.java, Runnable::class.java)
                    .build())
        }

        val initializerPreludeMutations = MutationAccumulatorImpl(CodeBlock::builder)

        context["envInitializerPreludeMutations"] = initializerPreludeMutations

        return false
    }
}
