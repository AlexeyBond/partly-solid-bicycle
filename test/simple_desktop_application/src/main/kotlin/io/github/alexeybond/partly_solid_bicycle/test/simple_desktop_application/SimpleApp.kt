package io.github.alexeybond.partly_solid_bicycle.test.simple_desktop_application

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.utils.Json
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.DefaultApplicationListener
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.state.TerminalStates
import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.DefaultContainer
import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.SingleApplicationHolder
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC
import java.io.FileInputStream
import java.util.concurrent.ConcurrentHashMap

fun main(args: Array<String>) {
    val conf = FileInputStream("lwjgl.json").use {
        Json().fromJson(LwjglApplicationConfiguration::class.java, it)
    }

    val container = DefaultContainer(ConcurrentHashMap())

    val state = TerminalStates.NULL

    IoC.use(SingleApplicationHolder())

    LwjglApplication(DefaultApplicationListener(state, container), conf);
}
