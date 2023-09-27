/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.event.game;

import net.minecraft.client.gui.screen.Screen;

public class OpenScreenEvent {
    private static final OpenScreenEvent INSTANCE = new OpenScreenEvent();

    public Screen screen;
    private boolean cancelled;

    public static OpenScreenEvent get(Screen screen) {
        INSTANCE.cancelled = false;
        INSTANCE.screen = screen;
        return INSTANCE;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
