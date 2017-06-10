package com.github.alexeybond.gdx_commons.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.github.alexeybond.gdx_commons.drawing.modules.GlobalDrawingState;
import com.github.alexeybond.gdx_commons.drawing.modules.GlobalParticlePool;
import com.github.alexeybond.gdx_commons.drawing.modules.ShaderLoader;
import com.github.alexeybond.gdx_commons.game.modules.GameSerialization;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.IoCContext;
import com.github.alexeybond.gdx_commons.ioc.Module;
import com.github.alexeybond.gdx_commons.resource_management.modules.ResourceManagement;
import com.github.alexeybond.gdx_commons.screen.modules.DefaultLoadingScreenModule;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 *
 */
public abstract class Application implements ApplicationListener {
    private AScreen currentScreen;
    private IoCContext ioCContext;
    private final ArrayList<Module> modules = new ArrayList<Module>();

    private AssetManager assetManager;
    private boolean isLoading = false;

    private static Collection<Module> getDefaultModules() {
        return Arrays.asList(
                new ResourceManagement()
                , new DefaultLoadingScreenModule()
                , new GlobalDrawingState()
                , new GlobalParticlePool()
                , new ShaderLoader()
                , new GameSerialization()
        );
    }

    protected abstract Collection<Module> getModules();

    @Override
    public void create() {
        ioCContext = new IoCContext();
        useContext();

        modules.addAll(getDefaultModules());
        modules.addAll(getModules());

        for (Module module : modules) module.init();

        assetManager = IoC.resolve("asset manager");

        enterScreen(IoC.<AScreen>resolve("initial screen"));
    }

    @Override
    public void resize(int width, int height) {
        useContext();
        currentScreen.resize(width, height);
    }

    @Override
    public void render() {
        try {
            useContext();
            nextScreen();
            checkLoadRequired();
            currentScreen.draw();
        } catch (RuntimeException e) {
            logCrash(e);
            throw e;
        }
    }

    @Override
    public void pause() {
        useContext();
        currentScreen.pause();
    }

    @Override
    public void resume() {
        useContext();
        currentScreen.resume();
    }

    @Override
    public void dispose() {
        useContext();
        currentScreen.leave(null);
        currentScreen.forget();

        for (int i = modules.size() - 1; i >= 0; i--) {
            modules.get(i).shutdown();
        }

        IoC.use(null);
    }

    private void nextScreen() {
        AScreen next;

        while ((next = currentScreen.next()) != null) {
            currentScreen.pause();
            currentScreen.leave(next);
            enterScreen(next);
        }
    }

    private void enterScreen(AScreen screen) {
        screen.enter(currentScreen);
        currentScreen = screen;
        screen.resume();
        screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void useContext() {
        IoC.use(ioCContext);
    }

    private void checkLoadRequired() {
        if (assetManager.getProgress() != 1f) {
            if (!isLoading) {
                enterScreen(IoC.<AScreen>resolve("loading screen"));
                isLoading = true;
            }
        } else {
            isLoading = false;
        }
    }

    private void logCrash(Throwable e) {
        try {
            PrintStream ps = new PrintStream(Gdx.files.external("crash-" + new Date() + ".log").write(false));
            try {
                e.printStackTrace(ps);
            } finally {
                ps.close();
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
}
