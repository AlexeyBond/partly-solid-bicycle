package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector.util

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.MetadataImpl
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.metadata.Metadata
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.reflection.MethodInfo
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.nullMetadata
import javax.tools.Diagnostic

interface SubscriptionMode {
    fun process(
            methodContext: ItemContext
    ): String
}

class BindingBasedSubscription(
        private val bindMode: BindMode,
        private val metadataMapping: MetadataImpl.(Metadata) -> Unit
) : SubscriptionMode {
    override fun process(methodContext: ItemContext): String {
        val info: MethodInfo = methodContext["info"]
        val componentContext: ItemContext = methodContext["componentContext"]
        val metadata = MetadataImpl(methodContext.context.env, nullMetadata)
        metadataMapping(metadata, info.metadata)

        val bindingExpression = bindMode.process(
                componentContext,
                metadata,
                info
        )

        return "$bindingExpression.getComponent()"
    }
}

object PropertySubscriptionMode : SubscriptionMode {
    override fun process(methodContext: ItemContext): String {
        val info: MethodInfo = methodContext["info"]
        val propertyName: String = info.metadata["method.subscribeProperty"] ?: run {
            methodContext.context.env.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "No property specified for method \"${info.name}\" to subscribe to",
                    info.declaringElements[0]
            )

            return "null"
        }

        return "component.$propertyName"
    }
}

val subscriptionModes: Map<String, SubscriptionMode> = mapOf(
        "path" to BindingBasedSubscription(PathBindMode, { original ->
            addData("property.bindPath=${original["method.subscribePath"]}")
        }),
        "attribute" to BindingBasedSubscription(AttributeBindMode, { original ->
            addData("property.bindAttribute=${original["method.subscribeAttribute"]}")
            addData("property.bindPathDefault=${original["method.subscribePathDefault"]}")
        }),
        "property" to PropertySubscriptionMode
)
