/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.event.events.game;

public class WindowResizedEvent {
    private static final WindowResizedEvent INSTANCE = new WindowResizedEvent();

    public static WindowResizedEvent get() {
        return INSTANCE;
    }
}
