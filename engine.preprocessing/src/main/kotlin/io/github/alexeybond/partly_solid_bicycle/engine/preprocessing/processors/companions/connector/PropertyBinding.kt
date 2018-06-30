package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector

import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNodePath
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.LinearOrder
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.generateAssignment
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.properties.PropertyInfo
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.operations.makeLocalName
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector.util.emitFieldProperty
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector.util.parsePathExpression
import javax.lang.model.element.Modifier
import javax.tools.Diagnostic

class PropertyBinding : ItemProcessor {
    val ORDER = LinearOrder(-10000)

    override fun getPriority(): Int {
        return -10000
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component-property" == itemKind
    }

    override fun processItem(context: ItemContext) {
        val pEnv = context.context.env
        val propertyInfo: PropertyInfo = context["info"]
        val componentContext: ItemContext = context["componentContext"]

        if (propertyInfo.metadata["property.bind"] != "true") return

        if (!propertyInfo.isWritable) {
            context.context.env.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Cannot bind non-writable property \"${propertyInfo.name}\"",
                    propertyInfo.declaringElements[0]
            )
        }

        val expressionText = propertyInfo.metadata["property.bindExpression"] ?: run {
            pEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Missing binding expression for property ${propertyInfo.name}.",
                    propertyInfo.declaringElements[0]
            )
            return
        }

        val parsedExpression = parsePathExpression(expressionText) ?: run {
            pEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Invalid binding expression for property ${propertyInfo.name}: \"$expressionText\"",
                    propertyInfo.declaringElements[0]
            )
            return
        }

        val pathType = pEnv.elementUtils.getTypeElement(LogicNodePath::class.java.canonicalName).asType()

        if (null != parsedExpression.property) {
            val pathPropertyName = makeLocalName("path_${propertyInfo.name}")

            val fieldMutations = emitFieldProperty(
                    componentContext,
                    pathPropertyName,
                    propertyInfo.metadata,
                    pathType,
                    propertyInfo.declaringElements,
                    listOf(
                            "property.serializedName=${propertyInfo.name}",
                            "property.bind=false"
                    )
            )

            fieldMutations.add {
                addModifiers(Modifier.PUBLIC)

            }
        }

        // TODO:: -------

        val connectorContext: ItemContext = componentContext["companion:connector"]

        val connectMut: Mutations<CodeBlock.Builder> =
                connectorContext["codeMutations:method:onConnected"]
        val disconnectMut: Mutations<CodeBlock.Builder> =
                connectorContext["codeMutations:method:onDisconnected"]

        connectMut.add(ORDER) {
            add("${propertyInfo.generateAssignment("component", "null" /*TODO::*/)}\n")
        }

        disconnectMut.add(ORDER.opposite) {
            if (!propertyInfo.hasSetter() || propertyInfo.metadata["property.forceReset"] == "true")
                add("${propertyInfo.generateAssignment("component", "null")}\n")
        }
    }
}
