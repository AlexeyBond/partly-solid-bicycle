package com.github.alexeybond.gdx_commons.music.modules;

import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.modules.Module;
import com.github.alexeybond.gdx_commons.ioc.strategy.Singleton;
import com.github.alexeybond.gdx_commons.music.impl.MusicPlayerImpl;

/**
 *
 */
public class GlobalMusicPlayer implements Module {
    private MusicPlayerImpl player;

    @Override
    public void init() {
        player = new MusicPlayerImpl();

        IoC.register("music player", new Singleton(player));
    }

    @Override
    public void shutdown() {
        player.stop();
    }
}
