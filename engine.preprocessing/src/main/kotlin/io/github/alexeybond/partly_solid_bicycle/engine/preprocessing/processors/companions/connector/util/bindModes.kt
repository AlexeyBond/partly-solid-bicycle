package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector.util

import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.TypeSpec
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNodePath
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.generatePathLiteral
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.properties.PropertyInfo
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.nodePathCN
import javax.lang.model.element.Modifier
import javax.tools.Diagnostic

interface BindMode {
    fun process(
            propertyContext: ItemContext
    ): String
}

object PathBindMode : BindMode {
    override fun process(propertyContext: ItemContext): String {
        val componentContext: ItemContext = propertyContext["componentContext"]
        val propertyInfo: PropertyInfo = propertyContext["info"]
        val implMutations: Mutations<TypeSpec.Builder> = componentContext["implMutations"]

        val pathStr = propertyInfo.metadata["property.bindPath"] ?: run {
            propertyContext.context.env.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "No binding path defined for property \"${propertyInfo.name}\"",
                    propertyInfo.declaringElements[0]
            )
            "."
        }

        val pathField = "path_${propertyInfo.name}"

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
    override fun process(propertyContext: ItemContext): String {
        val pEnv = propertyContext.context.env
        val componentContext: ItemContext = propertyContext["componentContext"]
        val propertyInfo: PropertyInfo = propertyContext["info"]

        val pathField = "path_${propertyInfo.name}"
        val pathType = pEnv.elementUtils.getTypeElement(LogicNodePath::class.java.canonicalName).asType()

        var serializedName = propertyInfo.metadata["property.bindAttribute"]
        if (serializedName == null || serializedName.isEmpty()) serializedName = propertyInfo.name

        val defaultPath = propertyInfo.metadata["property.bindPathDefault"]
        val hasDefaultPath = defaultPath != null && !defaultPath.isEmpty()
        val defaultPathField = "default_path_${propertyInfo.name}"

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
                propertyInfo.metadata,
                pathType,
                propertyInfo.declaringElements,
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
