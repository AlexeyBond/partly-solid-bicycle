package com.github.alexeybond.partly_solid_bicycle.util.parts;

import com.github.alexeybond.partly_solid_bicycle.util.parts.exceptions.PartConnectRejectedException;

/**
 *
 */
public interface Part<TOwner> {
    /**
     * Connect to the owner.
     *
     * When {@link PartConnectRejectedException} is thrown the part will be immediately disconnected
     * <em>without</em> {@link #onDisconnect(Object)} call.
     *
     * @throws PartConnectRejectedException if the part could not connect to the owner
     */
    void onConnect(TOwner owner);

    void onDisconnect(TOwner owner);
}
