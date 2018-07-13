package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector

import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.LinearOrder
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.generateAssignment
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.properties.PropertyInfo
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector.util.BindMode
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector.util.bindModes
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
            return
        }

        val modeName: String = propertyInfo.metadata["property.bindMode"] ?: run {
            context.context.env.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "No binding mode defined for property \"${propertyInfo.name}\"",
                    propertyInfo.declaringElements[0]
            )
            return
        }

        val mode: BindMode = bindModes[modeName] ?: run {
            context.context.env.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Unknown binding mode \"$modeName\" for property \"${propertyInfo.name}\"",
                    propertyInfo.declaringElements[0]
            )
            return
        }

        // TODO:: Bind non-node properties

        val expression = mode.process(context)

        val connectorContext: ItemContext = componentContext["companion:connector"]

        val connectMut: Mutations<CodeBlock.Builder> =
                connectorContext["codeMutations:method:onConnected"]
        val disconnectMut: Mutations<CodeBlock.Builder> =
                connectorContext["codeMutations:method:onDisconnected"]

        connectMut.add(ORDER) {
            add("${propertyInfo.generateAssignment("component", expression)}\n")
        }

        disconnectMut.add(ORDER.opposite) {
            if (!propertyInfo.hasSetter() || propertyInfo.metadata["property.forceReset"] == "true")
                add("${propertyInfo.generateAssignment("component", "null")}\n")
        }
    }
}
