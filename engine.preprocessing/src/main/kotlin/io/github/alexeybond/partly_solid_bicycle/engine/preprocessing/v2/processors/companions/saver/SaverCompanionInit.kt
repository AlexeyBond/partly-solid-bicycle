package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.processors.companions.saver

import com.squareup.javapoet.*
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Saver
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.OutputDataObject
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.processors.companions.shared.AbstractCompanionInit
import org.jetbrains.annotations.NotNull
import javax.lang.model.element.Modifier

class SaverCompanionInit : AbstractCompanionInit() {
    private val SAVER_CN = ClassName.get(Saver::class.java)
    private val ODO_CN = ClassName.get(OutputDataObject::class.java)

    override val companionType: String = "saver"
    override val companionSuffix: String = ""

    override fun getSuperinterfaces(componentCN: ClassName): List<TypeName> {
        return super.getSuperinterfaces(componentCN) + listOf(
                ParameterizedTypeName.get(SAVER_CN, componentCN)
        )
    }

    override fun setupMethods(
            componentCN: ClassName,
            method: (String, MethodSpec.Builder.() -> Unit) -> Unit) {
        method("save") {
            addParameter(ParameterSpec
                    .builder(componentCN, "src", Modifier.FINAL)
                    .addAnnotation(NotNull::class.java)
                    .build())

            addParameter(ParameterSpec
                    .builder(ODO_CN, "data", Modifier.FINAL)
                    .addAnnotation(NotNull::class.java)
                    .build())

            addModifiers(Modifier.PUBLIC)
            addAnnotation(Override::class.java)
        }
    }
}
