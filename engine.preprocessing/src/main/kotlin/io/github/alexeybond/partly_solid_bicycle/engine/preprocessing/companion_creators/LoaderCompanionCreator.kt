package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.companion_creators

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeSpec
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Loader
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.CompanionTypeCreator
import java.util.*
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class LoaderCompanionCreator : CompanionTypeCreator {
    override fun generateCompanion(
            companionType: String,
            className: ClassName,
            componentClass: TypeElement
    ): TypeSpec {
        val loadMethod = MethodSpec.methodBuilder("load")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override::class.java)
                .addParameter(ClassName.get(componentClass), "dst", Modifier.FINAL)
                .addParameter(ClassName.get(InputDataObject::class.java), "data", Modifier.FINAL)
                .addCode("") // TODO:: Generate ...
                .build()

        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(
                        ClassName.get(Loader::class.java), ClassName.get(componentClass)))
                .addMethod(loadMethod)
                .build()
    }

    override fun getCompanionTypes(): MutableIterable<String> {
        return Collections.singleton("loader")
    }
}
