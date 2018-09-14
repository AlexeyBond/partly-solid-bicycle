package io.github.alexeybond.partly_solid_bicycle.examples

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.DefaultApplicationListener
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.state.IoCDrivenApplicationState
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.state.TerminalStates
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ApplicationState
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.resource_management.ResourceReference
import io.github.alexeybond.partly_solid_bicycle.core.modules.application.AppConfigModule
import io.github.alexeybond.partly_solid_bicycle.core.modules.resource_management.GdxResourceTypeManagersModule
import io.github.alexeybond.partly_solid_bicycle.core.modules.resource_management.GlobalGdxAssetManagerModule
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.FromAttribute
import io.github.alexeybond.partly_solid_bicycle.game2d.impl.modules.InteractiveApplicationStateModule
import io.github.alexeybond.partly_solid_bicycle.game2d.impl.modules.ScreenFactoryModule
import io.github.alexeybond.partly_solid_bicycle.game2d.impl.modules.ScreenRenderTargetModule
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.Screen
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.app.screen.ScreenContext
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.RenderTarget

@Component(
        kind = "screen",
        name = arrayOf("resoursish screen"),
        modules = arrayOf(ResoursishScreenModule::class)
)
open class ResoursishScreen : Screen {
    @get:FromAttribute
    lateinit var image: ResourceReference<TextureRegion>

    private val batch = SpriteBatch()

    override fun create(context: ScreenContext, runState: ApplicationState): ApplicationState {
        return runState
    }

    override fun dispose() {
        batch.dispose()
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun render(target: RenderTarget) {
        target.begin()
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.projectionMatrix = batch.projectionMatrix.setToOrtho2D(
                0f, 0f,
                target.width.toFloat(), target.height.toFloat()
        )
        batch.begin()
        batch.draw(image.get(), 0f, 0f)
        batch.end()
        target.end()
    }
}

fun main(argv: Array<String>) {
    launchApp { env ->
        val ms = moduleSet(env) {
            defaultDemoModules()
            add(ScreenRenderTargetModule())
            add(InteractiveApplicationStateModule())
            add(ScreenFactoryModule())
            add(AppConfigModule("resource-manager/application.json"))
            add(GlobalGdxAssetManagerModule())
            add(GdxResourceTypeManagersModule())
            add(ResoursishScreenModule())
        }

        DefaultApplicationListener(IoCDrivenApplicationState(TerminalStates.EXIT), ms)
    }.run {
        logLevel = Application.LOG_DEBUG
    }
}
