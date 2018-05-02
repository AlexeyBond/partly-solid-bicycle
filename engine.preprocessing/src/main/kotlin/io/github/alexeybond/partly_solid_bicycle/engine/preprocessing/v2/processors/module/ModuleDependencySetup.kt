package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.processors.module

import com.squareup.javapoet.*
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.v2.add
import java.util.*
import javax.lang.model.element.Modifier

class ModuleDependencySetup : ItemProcessor {
    private val DEPENDENCY_INFO_FIELD_NAME = "__dependencies__"

    override fun getPriority(): Int {
        return -5_000
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "module" == itemKind
    }

    override fun processItem(context: ItemContext): Boolean {
        val implMutator: Mutations<TypeSpec.Builder> = context["implMutations"]

        val providedDependencies: List<String> = context["dependencies:provided"] ?: emptyList()
        val dependencies: List<String> = context["dependencies:direct"] ?: emptyList()
        val reverseDependencies: List<String> = context["dependencies:reverse"] ?: emptyList()

        fun stringVarargInitializer(list: List<String>): String {
            return list.map { s ->
                s
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\\n")
            }.joinToString(", ") { "\"$it\"" }
        }

        implMutator.add {
            addField(FieldSpec.builder(
                    ParameterizedTypeName.get(ClassName.get(Iterable::class.java),
                            ParameterizedTypeName.get(ClassName.get(Iterable::class.java),
                                    ClassName.get(String::class.java))),
                    DEPENDENCY_INFO_FIELD_NAME,
                    Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL
            ).build())

            addStaticBlock(CodeBlock.of("""$DEPENDENCY_INFO_FIELD_NAME =
            |${"\$T"}.<Iterable<String>>asList(
            |   ${"\$T"}.<String>asList(${stringVarargInitializer(providedDependencies)}),
            |   ${"\$T"}.<String>asList(${stringVarargInitializer(dependencies)}),
            |   ${"\$T"}.<String>asList(${stringVarargInitializer(reverseDependencies)})
            |);""".trimMargin(),
                    Arrays::class.java, Arrays::class.java, Arrays::class.java, Arrays::class.java))

            addMethod(MethodSpec
                    .methodBuilder("dependencyInfo")
                    .returns(ParameterizedTypeName.get(ClassName.get(Iterable::class.java),
                            ParameterizedTypeName.get(ClassName.get(Iterable::class.java),
                                    ClassName.get(String::class.java))))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addAnnotation(Override::class.java)
                    .addCode("return $DEPENDENCY_INFO_FIELD_NAME;\n")
                    .build())
        }

        return false
    }
}
