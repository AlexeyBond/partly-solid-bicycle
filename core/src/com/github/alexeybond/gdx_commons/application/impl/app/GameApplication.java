package com.github.alexeybond.gdx_commons.application.impl.app;

import com.github.alexeybond.gdx_commons.application.Application;
import com.github.alexeybond.gdx_commons.application.modules.DefaultLoadingScreenModule;
import com.github.alexeybond.gdx_commons.drawing.modules.GlobalDrawingState;
import com.github.alexeybond.gdx_commons.drawing.modules.GlobalParticlePool;
import com.github.alexeybond.gdx_commons.drawing.modules.ShaderLoader;
import com.github.alexeybond.gdx_commons.game.modules.CommonComponents;
import com.github.alexeybond.gdx_commons.game.modules.GameSerialization;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.modules.PhysicsComponentDeclarations;
import com.github.alexeybond.gdx_commons.game.systems.input.modules.InputComponentsDeclarations;
import com.github.alexeybond.gdx_commons.game.systems.render.modules.RenderComponentsDeclarations;
import com.github.alexeybond.gdx_commons.game.systems.tagging.modules.TaggingComponentsDeclarations;
import com.github.alexeybond.gdx_commons.ioc.modules.Modules;
import com.github.alexeybond.gdx_commons.music.modules.GlobalMusicPlayer;
import com.github.alexeybond.gdx_commons.resource_management.modules.ResourceManagement;

/**
 * Application that loads some common modules for 2-d games.
 */
public abstract class GameApplication extends Application {
    @Override
    protected Modules setupModules(Modules modules) {
        modules = super.setupModules(modules);

        modules.add(new ResourceManagement());
        modules.add(new GlobalDrawingState());
        modules.add(new GlobalParticlePool());
        modules.add(new ShaderLoader());
        modules.add(new GameSerialization());
        modules.add(new DefaultLoadingScreenModule());
        modules.add(new GlobalMusicPlayer());

        modules.add(new PhysicsComponentDeclarations());
        modules.add(new InputComponentsDeclarations());
        modules.add(new RenderComponentsDeclarations());
        modules.add(new TaggingComponentsDeclarations());
        modules.add(new CommonComponents());

        return modules;
    }
}
