package com.github.alexeybond.gdx_commons.util.parts;

/**
 *
 */
public interface Part<TOwner> {
    void onConnect(TOwner owner);

    void onDisconnect(TOwner owner);
}
