package com.github.alexeybond.gdx_commons.game.parts;

/**
 *
 */
public interface Part<TOwner> {
    void onConnect(TOwner owner);

    void onDisconnect(TOwner owner);
}
