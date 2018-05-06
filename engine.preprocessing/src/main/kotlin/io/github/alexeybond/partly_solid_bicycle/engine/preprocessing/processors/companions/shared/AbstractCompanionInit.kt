package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.shared

import com.squareup.javapoet.*
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Companion
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionResolver
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.impl.SingletonCompanionResolver
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.MutationAccumulatorImpl
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.applyMutation
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import javax.lang.model.element.Modifier

abstract class AbstractCompanionInit : ItemProcessor {
    val RESOLVER_FIELD_NAME = "RESOLVER"

    override fun getPriority(): Int {
        return -1_000
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component" == itemKind
    }

    override fun processItem(context: ItemContext) {
        val componentCN: ClassName = context["className"]
        val componentImplCN: ClassName = context["implClassName"]

        val companionCN = companionClassName(componentCN)

        val companionCtx = context.context.newItem("companion:$companionType$companionSuffix:$componentCN")

        companionCtx.run {
            set("kind", "component-companion")
            set("companionType", companionType)
            set("component", context)
            set("envs", getEnvironments())
            set("className", companionCN)
            set("implClassName", companionCN)
        }

        context["companion:$companionType$companionSuffix"] = companionCtx

        val companionCM: Mutations<TypeSpec.Builder> = context.context.addGenerated(
                companionCN,
                TypeSpec.classBuilder(companionCN)
        )

        classSetup(context, companionCtx)

        val resolverInit = resolverInitCodeMutation(RESOLVER_FIELD_NAME, companionCN, companionCtx)

        setupMethods(componentImplCN) { methodName, m ->
            val builder = MethodSpec.methodBuilder(methodName)

            m(builder)

            val codeMutations = MutationAccumulatorImpl(CodeBlock::builder)

            companionCM.add {
                addMethod(builder
                        .addCode(codeMutations.applyAll().build())
                        .build())
            }

            companionCtx["codeMutations:method:$methodName"] = codeMutations
        }

        companionCM.add {
            addField(FieldSpec.builder(
                    ParameterizedTypeName.get(
                            ClassName.get(CompanionResolver::class.java),
                            componentImplCN,
                            companionCN
                    ),
                    RESOLVER_FIELD_NAME)
                    .addModifiers(Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
                    .build()
            )

            addStaticBlock(CodeBlock.builder().applyMutation(resolverInit).build())

            addSuperinterfaces(getSuperinterfaces(componentImplCN))

            addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        }

        context.context.processItem(companionCtx)
    }

    protected abstract val companionType: String
    protected abstract val companionSuffix: String

    protected open fun companionClassName(componentCN: ClassName): ClassName {
        return ClassName.get(
                "generated.${componentCN.packageName()}",
                "${
                componentCN.simpleNames().joinToString(separator = "$")
                }_$companionType$companionSuffix"
        )
    }

    protected open fun resolverInitCodeMutation(
            resolverLvalue: String,
            companionCN: ClassName,
            companionCtx: ItemContext)
            : CodeBlock.Builder.() -> Unit {
        return {
            add("$resolverLvalue = new \$T(new \$T());",
                    SingletonCompanionResolver::class.java,
                    companionCN
            )
        }
    }

    protected open fun classSetup(componentCtx: ItemContext, companionCtx: ItemContext) {
    }

    protected open fun getSuperinterfaces(implementationCN: ClassName): List<TypeName> {
        return listOf(ParameterizedTypeName.get(
                ClassName.get(Companion::class.java),
                implementationCN
        ))
    }

    protected open fun setupMethods(
            implementationCN: ClassName,
            method: (String, MethodSpec.Builder.() -> Unit) -> Unit) {
    }

    protected open fun getEnvironments(): List<String> {
        return listOf("default")
    }
}
