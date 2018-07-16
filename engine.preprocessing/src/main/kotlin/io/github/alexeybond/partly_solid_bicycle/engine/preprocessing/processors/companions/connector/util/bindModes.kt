package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector.util

import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.TypeSpec
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNodePath
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.generatePathLiteral
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.metadata.Metadata
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.reflection.ReflectionInfo
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.nodePathCN
import javax.lang.model.element.Modifier
import javax.tools.Diagnostic

interface BindMode {
    fun process(
            componentContext: ItemContext,
            metadata: Metadata,
            origin: ReflectionInfo
    ): String
}

object PathBindMode : BindMode {
    override fun process(
            componentContext: ItemContext,
            metadata: Metadata,
            origin: ReflectionInfo
    ): String {
        val implMutations: Mutations<TypeSpec.Builder> = componentContext["implMutations"]

        val pathStr = metadata["property.bindPath"] ?: run {
            componentContext.context.env.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "No binding path defined for ${origin.kind} \"${origin.name}\"",
                    origin.declaringElements[0]
            )
            "."
        }

        val pathField = "path_${origin.kind}_${origin.name}"

        implMutations.add {
            addField(
                    FieldSpec
                            .builder(nodePathCN, pathField, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .initializer(generatePathLiteral(pathStr))
                            .build()
            )
        }

        return "component.$pathField.lookup(node)"
    }
}

object AttributeBindMode : BindMode {
    override fun process(
            componentContext: ItemContext,
            metadata: Metadata,
            origin: ReflectionInfo
    ): String {
        val pEnv = componentContext.context.env

        val pathField = "path_${origin.kind}_${origin.name}"
        val pathType = pEnv.elementUtils.getTypeElement(LogicNodePath::class.java.canonicalName).asType()

        var serializedName = metadata["property.bindAttribute"]
        if (serializedName == null || serializedName.isEmpty()) serializedName = origin.name

        val defaultPath = metadata["property.bindPathDefault"]
        val hasDefaultPath = defaultPath != null && !defaultPath.isEmpty()
        val defaultPathField = "default_path_${origin.kind}_${origin.name}"

        if (hasDefaultPath) {
            val implMutations: Mutations<TypeSpec.Builder> = componentContext["implMutations"]

            implMutations.add {
                addField(
                        FieldSpec
                                .builder(nodePathCN,
                                        defaultPathField,
                                        Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                                .initializer(generatePathLiteral(defaultPath!!))
                                .build()
                )
            }
        }

        emitFieldProperty(
                componentContext,
                pathField,
                metadata,
                pathType,
                origin.declaringElements,
                listOf(
                        "property.bind=false",
                        "property.serializedName=$serializedName",
                        "property.isOptional=${if (hasDefaultPath) "true" else "false"}"
                )
        ).add {
            addModifiers(Modifier.PUBLIC)

            if (hasDefaultPath) {
                initializer(defaultPathField)
            }
        }

        return "component.$pathField.lookup(node)"
    }
}

val bindModes: MutableMap<String, BindMode> = HashMap(mapOf(
        "path" to PathBindMode,
        "attribute" to AttributeBindMode
))
