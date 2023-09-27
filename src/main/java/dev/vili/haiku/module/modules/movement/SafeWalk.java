/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.module.modules.movement;

import dev.vili.haiku.event.events.entity.player.ClipAtLedgeEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;

import org.lwjgl.glfw.GLFW;

public class SafeWalk extends Module {
    public SafeWalk() {
        super("SafeWalk", "Prevents you from walking off blocks.", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        ClipAtLedgeEvent.get().reset();
        super.onDisable();
    }

    @HaikuSubscribe
    private void onClipAtLedge(ClipAtLedgeEvent event) {
        if (!mc.player.isSneaking()) {
            event.setClip(true);
            this.onDisable();
        }
    }
}
