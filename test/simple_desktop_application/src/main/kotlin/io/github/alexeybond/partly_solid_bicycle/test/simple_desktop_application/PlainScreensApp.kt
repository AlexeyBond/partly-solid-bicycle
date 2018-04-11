package io.github.alexeybond.partly_solid_bicycle.test.simple_desktop_application

import com.badlogic.gdx.Gdx
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.DefaultApplicationListener
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.module.BaseModule
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.state.IoCDrivenApplicationState
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.state.TerminalStates
import io.github.alexeybond.partly_solid_bicycle.core.impl.events.sources.DummyEventSource
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ApplicationState
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory.GenericFactory
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventListener
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy
import io.github.alexeybond.partly_solid_bicycle.core.modules.application.AppConfigModule
import io.github.alexeybond.partly_solid_bicycle.game2d.impl.modules.InteractiveApplicationStateModule
import io.github.alexeybond.partly_solid_bicycle.game2d.impl.modules.ScreenFactoryModule
import io.github.alexeybond.partly_solid_bicycle.game2d.impl.modules.ScreenRenderTargetModule
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.Screen
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenContext
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.RenderTarget
import java.util.*
import kotlin.concurrent.timerTask

class CustomScreen : Screen {
    override fun create(context: ScreenContext, runState: ApplicationState): ApplicationState {
        val listener = context.screenRoot["events", "event1"]
                .getComponent<EventListener<DummyEventSource>>()
        val source = DummyEventSource(1)
        source.subscribe(listener)

        Timer(true).schedule(timerTask {
            Gdx.app.postRunnable {
                source.trigger(null)
            }
        },
                2000)

        println("Custom screen created")
        return runState
    }

    override fun dispose() {
        println("Custom screen disposed")
    }

    override fun pause() {
        println("Custom screen paused")
    }

    override fun resume() {
        println("Custom screen resumed")
    }

    override fun render(target: RenderTarget) {

    }
}

class CustomScreenModule : BaseModule() {
    init {
        depend("ioc")

        reverseDepend("application_config")
    }

    override fun init(env: MutableCollection<Any>?) {
        IoC.register("custom screen", IoCStrategy {
            return@IoCStrategy GenericFactory<Screen, ScreenContext> { CustomScreen() }
        })
    }

    override fun shutdown() {

    }
}

fun main(args: Array<String>) {
    launchApp { env ->
        val ms = moduleSet(env) {
            defaultDemoModules()
            add(ScreenRenderTargetModule())
            add(InteractiveApplicationStateModule())
            add(AppConfigModule("simple-screens/application.json"))
            add(CustomScreenModule())
            add(ScreenFactoryModule())
        }

        DefaultApplicationListener(IoCDrivenApplicationState(TerminalStates.EXIT), ms)
    }
}
