package com.github.alexeybond.gdx_gm2.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.ch.SingleContextHolder;
import com.github.alexeybond.gdx_gm2.MyGdxGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        IoC.init(new SingleContextHolder());
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new MyGdxGame(), config);
    }
}
