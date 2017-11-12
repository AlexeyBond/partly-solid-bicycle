package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.companion_creators

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeSpec
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Loader
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.CompanionTypeCreator
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.FieldLoadGenerator
import java.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.tools.Diagnostic

class LoaderCompanionCreator : CompanionTypeCreator {
    private val STOP_MODIFIERS = setOf(Modifier.PRIVATE, Modifier.TRANSIENT, Modifier.FINAL, Modifier.STATIC)
    private val PARAM_DST = "dst"
    private val PARAM_DATA = "data"

    private val generators = ServiceLoader
            .load(FieldLoadGenerator::class.java, javaClass.classLoader).toList()

    override fun generateCompanion(
            processingEnvironment: ProcessingEnvironment,
            companionType: String,
            className: ClassName,
            componentClass: TypeElement
    ): TypeSpec {
        var loadMethodBuilder = MethodSpec.methodBuilder("load")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override::class.java)
                .addParameter(ClassName.get(componentClass), PARAM_DST, Modifier.FINAL)
                .addParameter(ClassName.get(InputDataObject::class.java), PARAM_DATA, Modifier.FINAL)

        loadMethodBuilder = writeBody(processingEnvironment, componentClass, loadMethodBuilder)

        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(
                        ClassName.get(Loader::class.java), ClassName.get(componentClass)))
                .addMethod(loadMethodBuilder.build())
                .build()
    }

    override fun getCompanionTypes(): MutableIterable<String> {
        return Collections.singleton("loader")
    }

    private fun writeBody(
            processingEnvironment: ProcessingEnvironment,
            componentClass: TypeElement,
            builder: MethodSpec.Builder
    ): MethodSpec.Builder {
        var b = builder
        componentClass.enclosedElements
                .filter { elem -> elem.kind == ElementKind.FIELD }
                .map { it as VariableElement }
                .filter { field -> field.modifiers.intersect(STOP_MODIFIERS).isEmpty() }
                .forEach { field ->
                    // TODO:: Add annotation changing serialized field name
                    val lvalue = "$PARAM_DST.${field.simpleName}"
                    val rvalue = "$PARAM_DATA.getField(\"${field.simpleName}\")"

                    for (generator in generators) {
                        generator.generateRead(processingEnvironment, field, lvalue, rvalue)?.let { code ->
                            // TODO:: Add annotation marking field as non-required (or guess from initializer)
                            b = b
                                    .beginControlFlow("")
                                    .addCode(code)
                                    .endControlFlow()
                            return@forEach
                        }
                    }

                    processingEnvironment.messager.printMessage(Diagnostic.Kind.ERROR,
                            "Cannot generate load operation for field.", field)
                }

        return b
    }
}
