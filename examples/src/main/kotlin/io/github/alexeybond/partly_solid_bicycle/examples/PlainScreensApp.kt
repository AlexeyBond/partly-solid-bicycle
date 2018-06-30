package io.github.alexeybond.partly_solid_bicycle.examples

import com.badlogic.gdx.Gdx
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.DefaultApplicationListener
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.state.IoCDrivenApplicationState
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.state.TerminalStates
import io.github.alexeybond.partly_solid_bicycle.core.impl.data.NullInputDataObject
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ApplicationState
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Channel
import io.github.alexeybond.partly_solid_bicycle.core.modules.application.AppConfigModule
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component
import io.github.alexeybond.partly_solid_bicycle.game2d.impl.modules.InteractiveApplicationStateModule
import io.github.alexeybond.partly_solid_bicycle.game2d.impl.modules.ScreenFactoryModule
import io.github.alexeybond.partly_solid_bicycle.game2d.impl.modules.ScreenRenderTargetModule
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.Screen
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenContext
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.RenderTarget
import java.util.*
import kotlin.concurrent.timerTask

@Component(
        kind = "screen",
        name = ["custom screen"],
        modules = [(CustomScreenModule::class)]
)
open class CustomScreen : Screen {
    override fun create(context: ScreenContext, runState: ApplicationState): ApplicationState {
        val channel = context.screenRoot["events", "event1"]
                .getComponent<Channel<InputDataObject>>()

        Timer(true).schedule(timerTask {
            Gdx.app.postRunnable {
                channel.send(NullInputDataObject.INSTANCE)
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
