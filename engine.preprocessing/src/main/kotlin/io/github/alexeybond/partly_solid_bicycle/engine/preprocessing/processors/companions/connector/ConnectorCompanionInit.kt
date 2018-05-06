package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.connector

import com.squareup.javapoet.*
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ComponentConnector
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.processors.companions.shared.AbstractCompanionInit
import org.jetbrains.annotations.NotNull
import javax.lang.model.element.Modifier

class ConnectorCompanionInit : AbstractCompanionInit() {
    private val CONNECTOR_CN = ClassName.get(ComponentConnector::class.java)
    private val LN_CN = ClassName.get(LogicNode::class.java)
    private val LNID_CN = ParameterizedTypeName.get(Id::class.java, LogicNode::class.java)

    override val companionType: String = "connector"
    override val companionSuffix: String = ""

    override fun getSuperinterfaces(implementationCN: ClassName): List<TypeName> {
        return super.getSuperinterfaces(implementationCN) + listOf(
                ParameterizedTypeName.get(CONNECTOR_CN, implementationCN)
        )
    }

    override fun setupMethods(
            implementationCN: ClassName,
            method: (String, MethodSpec.Builder.() -> Unit) -> Unit) {
        method("onConnected") {
            addParameter(
                    ParameterSpec
                            .builder(implementationCN, "component", Modifier.FINAL)
                            .addAnnotation(NotNull::class.java)
                            .build()
            )
            addParameter(
                    ParameterSpec
                            .builder(LN_CN, "node", Modifier.FINAL)
                            .addAnnotation(NotNull::class.java)
                            .build()
            )
            addParameter(
                    ParameterSpec
                            .builder(LNID_CN, "id", Modifier.FINAL)
                            .addAnnotation(NotNull::class.java)
                            .build()
            )

            addModifiers(Modifier.PUBLIC)
            addAnnotation(Override::class.java)
        }

        method("onDisconnected") {
            addParameter(
                    ParameterSpec
                            .builder(implementationCN, "component", Modifier.FINAL)
                            .addAnnotation(NotNull::class.java)
                            .build()
            )
            addParameter(
                    ParameterSpec
                            .builder(LN_CN, "node", Modifier.FINAL)
                            .addAnnotation(NotNull::class.java)
                            .build()
            )

            addModifiers(Modifier.PUBLIC)
            addAnnotation(Override::class.java)
        }
    }
}
