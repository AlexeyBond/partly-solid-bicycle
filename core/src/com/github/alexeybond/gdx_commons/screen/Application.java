package com.github.alexeybond.gdx_commons.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.github.alexeybond.gdx_commons.drawing.DrawingState;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.IoCContext;

/**
 *
 */
public abstract class Application implements ApplicationListener {
    private AScreen currentScreen;
    private IoCContext ioCContext;

    private DrawingState drawingState;

    protected abstract AScreen initialScreen();

    protected DrawingState drawingState() {
        return drawingState;
    }

    @Override
    public void create() {
        ioCContext = new IoCContext();
        useContext();

        drawingState = new DrawingState();

        currentScreen = initialScreen();
        currentScreen.enter(null);
        currentScreen.resume();
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
