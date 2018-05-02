package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.processors.companions.shared

import com.squareup.javapoet.*
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Companion
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionResolver
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.impl.SingletonCompanionResolver
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.MutationAccumulatorImpl
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.applyMutation
import javax.lang.model.element.Modifier

abstract class AbstractCompanionInit : ItemProcessor {
    val RESOLVER_FIELD_NAME = "RESOLVER"

    override fun getPriority(): Int {
        return -1_000
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component" == itemKind
    }

    override fun processItem(context: ItemContext): Boolean {
        val componentCN: ClassName = context["className"]

        val companionCN = companionClassName(componentCN)

        val companionCtx = context.context.newItem("companion:$companionType$companionSuffix:$componentCN")

        companionCtx.run {
            set("kind", "component-companion")
            set("companionType", companionType)
            set("component", context)
        }

        context["companion:$companionType$companionSuffix"] = companionCtx

        val companionCM: Mutations<TypeSpec.Builder> = context.context.addGenerated(
                companionCN,
                TypeSpec.classBuilder(companionCN)
        )

        classSetup(context, companionCtx)

        val resolverInit = resolverInitCodeMutation(RESOLVER_FIELD_NAME, companionCN)

        setupMethods(componentCN) { methodName, m ->
            val builder = MethodSpec.methodBuilder(methodName)

            m(builder)

            val codeMutations = MutationAccumulatorImpl(CodeBlock.builder())

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
                            componentCN,
                            companionCN
                    ),
                    RESOLVER_FIELD_NAME)
                    .addModifiers(Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
                    .build()
            )

            addStaticBlock(CodeBlock.builder().applyMutation(resolverInit).build())

            addSuperinterfaces(getSuperinterfaces(componentCN))

            addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        }

        context.context.processItem(companionCtx)

        return false
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

    protected open fun resolverInitCodeMutation(resolverLvalue: String, companionCN: ClassName)
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

    protected open fun getSuperinterfaces(componentCN: ClassName): List<TypeName> {
        return listOf(ParameterizedTypeName.get(
                ClassName.get(Companion::class.java),
                componentCN
        ))
    }

    protected open fun setupMethods(
            componentCN: ClassName,
            method: (String, MethodSpec.Builder.() -> Unit) -> Unit) {
    }
}
