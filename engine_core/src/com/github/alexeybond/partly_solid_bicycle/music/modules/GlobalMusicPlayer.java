package com.github.alexeybond.partly_solid_bicycle.music.modules;

import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Module;
import com.github.alexeybond.partly_solid_bicycle.ioc.strategy.Singleton;
import com.github.alexeybond.partly_solid_bicycle.music.impl.MusicPlayerImpl;

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
