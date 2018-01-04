package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.companion_creators

import com.squareup.javapoet.*
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionResolver
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Loader
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.impl.SingletonCompanionResolver
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.UndefinedFieldException
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotation_processors.COMPANION_RESOLVER_FIELD_NAME
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Optional
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.CompanionTypeCreator
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.FieldLoadGenerator
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.exceptions.NoLoadRequiredException
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

    private val rootGenerator = FieldLoadGenerator { env, targetType, lvalueExpr, rvalueExpr, rootGenerator ->
        try {
            for (generator in generators) {
                val generated = generator.generateRead(env, targetType, lvalueExpr, rvalueExpr, rootGenerator)
                if (null != generated) return@FieldLoadGenerator generated
            }
        } catch (e: NoLoadRequiredException) {
        }

        return@FieldLoadGenerator null
    }

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

        val resolverField = FieldSpec.builder(
                ParameterizedTypeName.get(ClassName.get(CompanionResolver::class.java),
                        TypeName.get(componentClass.asType()),
                        ParameterizedTypeName.get(ClassName.get(Loader::class.java),
                                TypeName.get(componentClass.asType()))),
                COMPANION_RESOLVER_FIELD_NAME)
                .addModifiers(Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
                .initializer("new \$T(new \$T())",
                        SingletonCompanionResolver::class.java, className)
                .build()

        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(
                        ClassName.get(Loader::class.java), ClassName.get(componentClass)))
                .addMethod(loadMethodBuilder.build())
                .addField(resolverField)
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
                    val serializedName = field.simpleName
                    val lvalue = "$PARAM_DST.${field.simpleName}"
                    val fieldVar = "__fld"
                    val fieldExpr = "$PARAM_DATA.getField(\"$serializedName\")"
                    val rvalue = fieldVar

                    rootGenerator.generateRead(
                            processingEnvironment,
                            field.asType(),
                            lvalue, rvalue, rootGenerator)
                            ?.let { code ->
                                // TODO:: Add more elegant way to declare optional field later
                                val optional = field.getAnnotation(Optional::class.java) != null
                                b = b
                                        .beginControlFlow("do")
                                        .addCode("\$T $fieldVar = null;\n",
                                                InputDataObject::class.java)
                                if (optional) b = b
                                        .beginControlFlow("try")
                                b = b
                                        .addCode("$fieldVar = $fieldExpr;\n")
                                if (optional) b = b
                                        .nextControlFlow("catch (\$T ignore)",
                                                UndefinedFieldException::class.java)
                                        .addCode("break;\n")
                                        .endControlFlow()
                                b = b
                                        .addCode(code)
                                        .endControlFlow("while(false);\n")
                                return@forEach
                            }

                    processingEnvironment.messager.printMessage(Diagnostic.Kind.ERROR,
                            "Cannot generate load operation for field.", field)
                }

        return b
    }
}
