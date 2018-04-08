package io.github.alexeybond.partly_solid_bicycle.test.simple_desktop_application

import com.badlogic.gdx.ApplicationListener
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.DefaultApplicationListener
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.module.BaseModule
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.state.TerminalStates

/// Application-specific code

class CustomModule : BaseModule() {
    init {
        provide("custom_things")
        depend("ioc")
    }

    override fun shutdown() {
        println("Custom module is shutting down")
    }

    override fun init(env: MutableCollection<Any>?) {
        println("Custom module is initializing; env is $env")
    }
}

fun createApplicationListener(env: List<String>): ApplicationListener {
    val msBuilder = moduleSet(env) {
        defaultDemoModules()
        add(CustomModule())
    }

    return DefaultApplicationListener(TerminalStates.NULL, msBuilder)
}

/// Platform-specific code

fun main(args: Array<String>) {
    launchApp { createApplicationListener(it) }
}
