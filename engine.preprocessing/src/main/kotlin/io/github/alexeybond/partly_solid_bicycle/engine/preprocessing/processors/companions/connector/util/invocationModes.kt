package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector.util

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.context.ItemContext
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.interfaces.reflection.MethodInfo
import javax.lang.model.element.ExecutableElement
import javax.tools.Diagnostic

interface InvocationMode {
    fun generateInvocation(
            method: MethodInfo,
            methodContext: ItemContext
    ): CodeBlock
}

object DefaultInvocationMode : InvocationMode {
    override fun generateInvocation(
            method: MethodInfo,
            methodContext: ItemContext
    ): CodeBlock {
        val element: ExecutableElement = method.declaringElements[0] as ExecutableElement
        val parameters = element.parameters
        val pEnv = methodContext.context.env
        val componentContext: ItemContext = methodContext["componentContext"]
        val componentImplCN: ClassName = componentContext["implClassName"]

        when (parameters.size) {
            0 ->
                return CodeBlock.of(
                        "\$T.this.${method.name}();\n",
                        componentImplCN
                )
            1 ->
                return CodeBlock.of(
                        "\$T.this.${method.name}((\$T) event);\n",
                        componentImplCN, parameters[0].asType()
                )
            2 -> {
                val topicType = pEnv.elementUtils.getTypeElement(Topic::class.java.canonicalName).asType()

                if (!pEnv.typeUtils.isAssignable(
                                pEnv.typeUtils.erasure(parameters[1].asType()),
                                topicType
                        )) {
                    pEnv.messager.printMessage(
                            Diagnostic.Kind.ERROR,
                            "Invalid type of second argument of event handling method \"${method.name}\": " +
                                    "expected subtype of a Topic<T>",
                            method.declaringElements[0]
                    )
                }

                return CodeBlock.of(
                        "\$T.this.${method.name}((\$T) event, (\$T) topic);\n",
                        componentImplCN,
                        parameters[0].asType(), parameters[1].asType()
                )
            }
            else -> {
                pEnv.messager.printMessage(
                        Diagnostic.Kind.ERROR,
                        "Invalid parameter count in event handling method \"${method.name}\": " +
                                "expected 0, 2 or 3",
                        method.declaringElements[0]
                )
                return CodeBlock.of("// ERROR\n")
            }
        }
    }
}

val invocationModes: Map<String, InvocationMode> = mapOf(
        "default" to DefaultInvocationMode
)
