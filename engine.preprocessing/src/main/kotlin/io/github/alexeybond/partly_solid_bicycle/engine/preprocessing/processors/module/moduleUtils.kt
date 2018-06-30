package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.module

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.TypeSpec
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.DEFAULT_ORDER
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.MutationAccumulatorImpl
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.mutation.Mutation

val ENV_INITIALIZERS_FIELD = "INITIALIZERS"

fun modifyModuleInit(moduleItem: ItemContext, env: String, mut: Mutation<CodeBlock.Builder>) {
    val initMutations: Mutations<CodeBlock.Builder> = moduleItem["envInitializerMutations:$env"]
            ?: createModuleEnvironmentInitializer(moduleItem, env)

    initMutations.addMutation(mut, DEFAULT_ORDER)
}

fun createModuleEnvironmentInitializer(
        moduleItem: ItemContext,
        env: String): Mutations<CodeBlock.Builder> {
    val mut = MutationAccumulatorImpl<CodeBlock.Builder>(CodeBlock::builder)

    val classMutations: Mutations<TypeSpec.Builder> = moduleItem["implMutations"]
    val preludeMutations: MutationAccumulatorImpl<CodeBlock.Builder> = moduleItem["envInitializerPreludeMutations"]

    classMutations.add {
        addStaticBlock(CodeBlock.builder().run {
            add("${ENV_INITIALIZERS_FIELD}.put(\$S, new \$T() {\n",
                    env, Runnable::class.java)
            add("@Override public void run() {\n")
            add(preludeMutations.applyAll().build())
            add(mut.applyAll().build())
            add("}\n")
            add("});\n")
            this
        }.build())
    }

    moduleItem["envInitializerMutations:$env"] = mut

    return mut
}
