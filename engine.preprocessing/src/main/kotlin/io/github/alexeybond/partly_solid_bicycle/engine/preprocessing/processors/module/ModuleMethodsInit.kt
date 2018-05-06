package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.module

import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeSpec
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.MutationAccumulatorImpl
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import javax.lang.model.element.Modifier

class ModuleMethodsInit : ItemProcessor {
    override fun acceptsItemKind(itemKind: String): Boolean {
        return "module" == itemKind
    }

    override fun getPriority(): Int {
        return -1_000
    }

    override fun processItem(context: ItemContext): Boolean {
        val moduleMutations: Mutations<TypeSpec.Builder> = context["implMutations"]

        val staticInitBuilder = MutationAccumulatorImpl<CodeBlock.Builder>(CodeBlock::builder)
        val initBuilder = MutationAccumulatorImpl<CodeBlock.Builder>(CodeBlock::builder)
        val shutdownBuilder = MutationAccumulatorImpl<CodeBlock.Builder>(CodeBlock::builder)

        context["codeMutations:staticInit"] = staticInitBuilder
        context["codeMutations:method:init"] = initBuilder
        context["codeMutations:method:shutdown"] = shutdownBuilder

        moduleMutations.add {
            addMethod(MethodSpec
                    .methodBuilder("init")
                    .addParameter(ParameterizedTypeName.get(
                            Collection::class.java, Object::class.java), "envs")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override::class.java)
                    .addCode(initBuilder.applyAll().build())
                    .build())

            addMethod(MethodSpec
                    .methodBuilder("shutdown")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override::class.java)
                    .addCode(shutdownBuilder.applyAll().build())
                    .build())

            addStaticBlock(staticInitBuilder.applyAll().build())
        }

        return false
    }
}
