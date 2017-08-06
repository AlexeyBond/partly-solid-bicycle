package com.github.alexeybond.partly_solid_bicycle.application;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.github.alexeybond.partly_solid_bicycle.drawing.Scene;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoCContext;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Modules;

import java.io.PrintStream;
import java.util.Date;

/**
 * This class is the basic implementation of LibGDX {@link ApplicationListener} for applications using
 * Partly Solid Bicycle(C)(R)(TM)(xD).
 *
 * <p>
 * Application manages global state ({@link IoC} context) and displayed screens.
 * </p>
 *
 * <p>There are two special kinds of screens the application class is aware of:</p>
 * <ul>
 *     <li>initial screen - the screen that becomes active as the application is created</li>
 *     <li>loading screen - the screen that is shown when some resources that are required
 *     by another screen are being loaded. Loading screen is responsible for running load
 *     progress manager on update.
 *     </li>
 * </ul>
 */
public class Application implements ApplicationListener {
    private Modules modules = new Modules();
    private IoCContext context;
    private LoadProgressManager loadProgressManager;
    private Screen currentScreen;
    private boolean isLoading = false;

    private void goToInitial(Screen screen) {
        screen.acquire();
        screen.enter(null);
        screen.unpause();
        currentScreen = screen;
    }

    private void changeScreen(Screen from, Screen to) {
        to.acquire();
        from.pause();
        from.leave(to);
        to.enter(from);
        to.unpause();
        from.release();
        currentScreen = to;
    }

    private void switchScreens() {
        for (;;) {
            Screen cur = currentScreen;
            Screen next = cur.next();
            cur.next(null);

            if (null == next || cur == next) return;

            changeScreen(cur, next);
        }
    }

    private void checkLoading() {
        if (null == loadProgressManager) return;

        if (!loadProgressManager.isCompleted()) {
            if (!isLoading) {
                changeScreen(currentScreen, IoC.<Screen>resolve("loading screen"));
                isLoading = true;
            }
        } else {
            isLoading = false;
        }
    }

    private void logCrash(Throwable e) {
        try {
            String crashLogFileName = "crash-" + new Date() + ".log";
            crashLogFileName = crashLogFileName.replace(':', '-');
            PrintStream ps = new PrintStream(Gdx.files.external(crashLogFileName).write(false));
            try {
                e.printStackTrace(ps);
            } finally {
                ps.close();
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    @Override
    public void create() {
        context = new IoCContext();
        IoC.use(context);

        modules = setupModules(modules);

        try {
            loadProgressManager = IoC.resolve("load progress manager");
        } catch (Exception e) {
            Gdx.app.log("APP,WARN", "Application is running without load progress manager.");
        }

        goToInitial(IoC.<Screen>resolve("initial screen"));
    }

    @Override
    public void resize(int width, int height) {
        IoC.use(context);
        currentScreen.viewport().update(width, height);
    }

    @Override
    public void render() {
        try {
            IoC.use(context);
            switchScreens();
            checkLoading();
            currentScreen.update(Gdx.graphics.getDeltaTime());
            Scene scene = currentScreen.scene();
            scene.context().setOutputTarget(currentScreen.screenTarget());
            scene.draw();
        } catch (RuntimeException e) {
            logCrash(e);
            throw e;
        }
    }

    @Override
    public void pause() {
        IoC.use(context);
        currentScreen.pause();
    }

    @Override
    public void resume() {
        IoC.use(context);
        currentScreen.unpause();
    }

    @Override
    public void dispose() {
        IoC.use(context);

        currentScreen.release();
        currentScreen = null;

        modules.dispose();
    }

    protected Modules setupModules(Modules modules) {
        return modules;
    }
}
