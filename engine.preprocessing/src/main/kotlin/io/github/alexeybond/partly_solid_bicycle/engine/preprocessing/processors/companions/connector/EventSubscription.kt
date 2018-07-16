package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector

import com.squareup.javapoet.*
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.LinearOrder
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.Mutations
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.add
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.processor.ItemProcessor
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.reflection.MethodInfo
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector.util.InvocationMode
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector.util.SubscriptionMode
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector.util.invocationModes
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector.util.subscriptionModes
import org.jetbrains.annotations.NotNull
import javax.lang.model.element.Modifier
import javax.tools.Diagnostic

val subscriptionCN = ClassName.get(
        "io.github.alexeybond.partly_solid_bicycle.core.impl.event.listeners",
        "UniqueSubscription"
)!!
val objectJClass = java.lang.Object::class.java
val objectSubscriptionTN = ParameterizedTypeName.get(subscriptionCN, ClassName.OBJECT)!!
val objectTopicTN = ParameterizedTypeName.get(
        ClassName.get(Topic::class.java),
        WildcardTypeName.subtypeOf(TypeName.OBJECT)
)!!

class EventSubscription : ItemProcessor {
    val ORDER = LinearOrder(-1000)

    override fun getPriority(): Int {
        return 0
    }

    override fun acceptsItemKind(itemKind: String): Boolean {
        return "component-method" == itemKind
    }

    override fun processItem(context: ItemContext) {
        val info: MethodInfo = context["info"]

        if (info.metadata["method.subscribe"] != "true") return

        val componentContext: ItemContext = context["componentContext"]
        val componentImplMutations: Mutations<TypeSpec.Builder> = componentContext["implMutations"]

        val subscriptionModeName: String = info.metadata["method.subscribeMode"] ?: run {
            context.context.env.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "No subscription mode defined for method \"${info.name}\"",
                    info.declaringElements[0]
            )

            return
        }

        val subscriptionMode: SubscriptionMode = subscriptionModes[subscriptionModeName] ?: run {
            context.context.env.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Unknown subscription mode \"$subscriptionModeName\" defined " +
                            "for method \"${info.name}\"",
                    info.declaringElements[0]
            )

            return
        }

        val invocationModeName: String = info.metadata["method.subscribeInvocationMode"] ?: "default"

        val invocationMode: InvocationMode = invocationModes[invocationModeName] ?: run {
            context.context.env.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Unknown invocation mode \"$invocationModeName\" defined " +
                            "for method \"${info.name}\"",
                    info.declaringElements[0]
            )

            return
        }

        val topicExpression = subscriptionMode.process(context)

        val invocationStatement = invocationMode.generateInvocation(info, context)

        val subscriptionField = "subscription_${info.name}"

        val subscriptionInitializerBlock = CodeBlock.builder()
                .beginControlFlow("new \$T()", objectSubscriptionTN)
                .beginControlFlow("@\$T protected void doReceive(@\$T \$T event, @\$T \$T topic)",
                        Override::class.java,
                        NotNull::class.java, objectJClass, NotNull::class.java, objectTopicTN)
                .add(invocationStatement)
                .endControlFlow()
                .endControlFlow()
                .build()

        componentImplMutations.add {
            addField(
                    FieldSpec.builder(objectSubscriptionTN, subscriptionField,
                            Modifier.PUBLIC, Modifier.FINAL)
                            .initializer(subscriptionInitializerBlock)
                            .build()
            )
        }

        val connectorContext: ItemContext = componentContext["companion:connector"]

        val connectMut: Mutations<CodeBlock.Builder> =
                connectorContext["codeMutations:method:onConnected"]
        val disconnectMut: Mutations<CodeBlock.Builder> =
                connectorContext["codeMutations:method:onDisconnected"]

        connectMut.add(ORDER) {
            beginControlFlow("")
            add("\$T topic = $topicExpression;\n",
                    objectTopicTN)
            add("component.$subscriptionField.subscribe(topic);\n")
            endControlFlow()
        }

        disconnectMut.add(ORDER.opposite) {
            add("component.$subscriptionField.clear();\n")
        }
    }
}
