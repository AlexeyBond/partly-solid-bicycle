package com.github.alexeybond.gdx_commons.application.impl.layers;

import com.badlogic.gdx.audio.Music;
import com.github.alexeybond.gdx_commons.application.Screen;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.music.MusicPlayer;

public class MusicLayer extends LayerAdapter {
    private final String musicName;
    private final boolean loop;

    private MusicPlayer player;

    public MusicLayer() {
        this(null, false);
    }

    public MusicLayer(String musicName, boolean loop) {
        this.musicName = musicName;
        this.loop = loop;
    }

    @Override
    public void onConnect(Screen screen) {
        player = IoC.resolve("music player");
        if (null != musicName) {
            Music music = IoC.resolve("load music", musicName);

            player.enqueue(music, loop).next();
        }
    }

    @Override
    public void onDisconnect(Screen screen) {

    }

    @Override
    public void update(float dt) {
        player.update(dt);
    }

    public MusicPlayer player() {
        return player;
    }
}
