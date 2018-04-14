package io.github.alexeybond.partly_solid_bicycle.examples

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.utils.Json
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.module.ModuleSetBuilder
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode
import io.github.alexeybond.partly_solid_bicycle.core.modules.CoreDefault
import io.github.alexeybond.partly_solid_bicycle.core.modules.DeclarativeNodeFactories
import io.github.alexeybond.partly_solid_bicycle.core.modules.application.AppRootModule
import io.github.alexeybond.partly_solid_bicycle.core.modules.ioc.NonThreadSafeIoCContainerModule
import io.github.alexeybond.partly_solid_bicycle.core.modules.ioc.SingleApplicationIoCHolderModule
import java.io.FileInputStream

fun launchApp(app: (List<String>) -> ApplicationListener) {
    val conf = FileInputStream("lwjgl.json").use {
        Json().fromJson(LwjglApplicationConfiguration::class.java, it)
    }

    val environmentFlags = listOf("platform:desktop")

    val listener = app(environmentFlags)

    LwjglApplication(listener, conf)
}

fun moduleSet(env: List<String>, ini: ModuleSetBuilder.() -> Unit): ModuleSetBuilder {
    val msb = ModuleSetBuilder()
    ini(msb)
    env.forEach { msb.addEnv(it) }
    return msb
}

fun ModuleSetBuilder.defaultDemoModules() {
    add(SingleApplicationIoCHolderModule())
    add(NonThreadSafeIoCContainerModule())
    add(DeclarativeNodeFactories())
    add(CoreDefault())
    add(AppRootModule())
}

operator fun LogicNode.get(vararg path: String): LogicNode {
    val idSet = treeContext.idSet
    var node = this

    for (step in path) node = node[idSet[step]]

    return node
}
