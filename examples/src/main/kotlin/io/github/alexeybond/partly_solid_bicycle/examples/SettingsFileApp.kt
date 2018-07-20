package io.github.alexeybond.partly_solid_bicycle.examples

import com.badlogic.gdx.Gdx
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.DefaultApplicationListener
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.state.TerminalStates
import io.github.alexeybond.partly_solid_bicycle.core.impl.data.gdxjson.GdxJsonDataReader
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables.ObjectVariable
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode
import io.github.alexeybond.partly_solid_bicycle.core.modules.application.AppConfigModule
import java.util.*
import kotlin.concurrent.timerTask

fun main(args: Array<String>) {
    launchApp { env ->
        val ms = moduleSet(env) {
            defaultDemoModules()
            add(AppConfigModule("settings-files/application.json"))
        }

        Timer(true).schedule(timerTask {
            Gdx.app.postRunnable {
                IoC.resolve<LogicNode>("application root node")["config", "settings", "test.key_4"]
                        .getComponent<ObjectVariable<InputDataObject>>()
                        .set(GdxJsonDataReader().parseData("\"hello, settings\""))
            }
        }, 10000)

        DefaultApplicationListener(TerminalStates.NULL, ms)
    }
}
