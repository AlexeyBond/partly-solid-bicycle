package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.component

import com.squareup.javapoet.*
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Companion
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionOwner
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionResolver
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.MutableClassCompanionResolver
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import org.jetbrains.annotations.NotNull
import javax.lang.model.element.Modifier

class CompanionOwnershipInit : ItemProcessor {
    val COWNER_CN = ClassName.get(CompanionOwner::class.java)
    val COMPNN_CN = ClassName.get(Companion::class.java)
    val COMRES_CN = ClassName.get(CompanionResolver::class.java)
    val MCLASSCOMRES_CN = ClassName.get(MutableClassCompanionResolver::class.java)

    val RESOLVER_FIELD_NAME = "RESOLVER"

    override fun getPriority(): Int {
        return -1_800
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component" == itemKind
    }

    override fun processItem(context: ItemContext) {
        val implCN: ClassName = context["implClassName"]
        val implMutations: Mutations<TypeSpec.Builder> = context["implMutations"]

        context["companionResolverField"] = RESOLVER_FIELD_NAME

        implMutations.add {
            addSuperinterface(ParameterizedTypeName.get(COWNER_CN, implCN))

            val companionTypeTV = TypeVariableName.get("T", ParameterizedTypeName.get(
                    COMPNN_CN, implCN
            ))

            val classCRTN = ParameterizedTypeName.get(MCLASSCOMRES_CN, implCN)

            addField(FieldSpec
                    .builder(classCRTN, RESOLVER_FIELD_NAME,
                            Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                    .addAnnotation(NotNull::class.java)
                    .build())

            addMethod(MethodSpec
                    .methodBuilder("getCompanionObject")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addParameter(ParameterSpec
                            .builder(String::class.java, "name", Modifier.FINAL)
                            .addAnnotation(NotNull::class.java)
                            .build())
                    .addAnnotation(Override::class.java)
                    .addAnnotation(NotNull::class.java)
                    .addTypeVariable(companionTypeTV)
                    .returns(companionTypeTV)
                    .addCode("return $RESOLVER_FIELD_NAME.<T>resolve(name).resolve(this);")
                    .build())

            addMethod(MethodSpec
                    .methodBuilder("getClassCompanionResolver")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addAnnotation(NotNull::class.java)
                    .returns(classCRTN)
                    .addCode("return $RESOLVER_FIELD_NAME;")
                    .build())
        }
    }
}
