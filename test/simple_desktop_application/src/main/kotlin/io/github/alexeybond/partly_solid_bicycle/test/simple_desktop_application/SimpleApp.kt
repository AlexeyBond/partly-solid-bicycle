package io.github.alexeybond.partly_solid_bicycle.test.simple_desktop_application

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.utils.Json
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.DefaultApplicationListener
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.module.ModuleSetBuilder
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.state.TerminalStates
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.Module
import io.github.alexeybond.partly_solid_bicycle.core.modules.CoreDefault
import io.github.alexeybond.partly_solid_bicycle.core.modules.DeclarativeNodeFactories
import io.github.alexeybond.partly_solid_bicycle.core.modules.ioc.NonThreadSafeIoCContainerModule
import io.github.alexeybond.partly_solid_bicycle.core.modules.ioc.SingleApplicationIoCHolderModule
import java.io.FileInputStream

/// Application-specific code

class CustomModule : Module {
    override fun shutdown() {
        println("Custom module is shutting down")
    }

    override fun dependencyInfo(): Iterable<Iterable<String>> {
        return listOf(listOf("custom_things"), listOf("ioc"), listOf())
    }

    override fun init(env: MutableCollection<Any>?) {
        println("Custom module is initializing; env is ${env}")
    }
}

fun createApplicationListener(env: List<String>): ApplicationListener {
    var msBuilder = ModuleSetBuilder()
            .add(SingleApplicationIoCHolderModule())
            .add(NonThreadSafeIoCContainerModule())
            .add(DeclarativeNodeFactories())
            .add(CoreDefault())
            .add(CustomModule())

    env.forEach({ msBuilder = msBuilder.addEnv(it) })

    return DefaultApplicationListener(TerminalStates.NULL, msBuilder)
}

/// Platform-specific code

fun main(args: Array<String>) {
    val conf = FileInputStream("lwjgl.json").use {
        Json().fromJson(LwjglApplicationConfiguration::class.java, it)
    }

    val environmentFlags = listOf("platform:desktop")

    val listener = createApplicationListener(environmentFlags)

    LwjglApplication(listener, conf)
}
