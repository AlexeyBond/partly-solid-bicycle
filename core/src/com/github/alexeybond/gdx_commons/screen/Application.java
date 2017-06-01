package com.github.alexeybond.gdx_commons.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.IoCContext;

/**
 *
 */
public abstract class Application implements ApplicationListener {
    private AScreen currentScreen;
    private IoCContext ioCContext;

    protected abstract AScreen initialScreen();

    @Override
    public void create() {
        ioCContext = new IoCContext();
        useContext();

        currentScreen = initialScreen();
    }

    @Override
    public void resize(int width, int height) {
        useContext();
        currentScreen.resize(width, height);
    }

    @Override
    public void render() {
        useContext();
        nextScreen();
        currentScreen.draw();
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
    }

    private void nextScreen() {
        AScreen next;

        while ((next = currentScreen.next()) != null) {
            currentScreen.pause();
            currentScreen.leave(next);
            next.enter(currentScreen);
            next.resume();
            next.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    private void useContext() {
        IoC.use(ioCContext);
    }
}
