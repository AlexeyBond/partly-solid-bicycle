package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.loader

import com.squareup.javapoet.*
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Loader
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.shared.AbstractCompanionInit
import org.jetbrains.annotations.NotNull
import javax.lang.model.element.Modifier

class LoaderCompanionInit : AbstractCompanionInit() {
    private val LOADER_CN = ClassName.get(Loader::class.java)
    private val IDO_CN = ClassName.get(InputDataObject::class.java)

    override val companionType: String = "loader"
    override val companionSuffix: String = ""

    override fun getSuperinterfaces(implementationCN: ClassName): List<TypeName> {
        return super.getSuperinterfaces(implementationCN) + listOf<TypeName>(
                ParameterizedTypeName.get(LOADER_CN, implementationCN))
    }

    override fun setupMethods(
            implementationCN: ClassName,
            method: (String, MethodSpec.Builder.() -> Unit) -> Unit) {
        method("load") {
            addParameter(
                    ParameterSpec.builder(implementationCN, "dst", Modifier.FINAL)
                            .addAnnotation(NotNull::class.java)
                            .build()
            )
            addParameter(
                    ParameterSpec.builder(IDO_CN, "data", Modifier.FINAL)
                            .addAnnotation(NotNull::class.java)
                            .build()
            )
            addModifiers(Modifier.PUBLIC)
            addAnnotation(Override::class.java)
        }
    }
}