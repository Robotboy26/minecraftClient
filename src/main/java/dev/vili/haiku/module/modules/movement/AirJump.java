/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.module.modules.movement;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.Haiku;
import dev.vili.haiku.event.events.KeyEvent;
import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.utils.HaikuLogger;
import dev.vili.haiku.utils.misc.input.KeyAction;
import meteordevelopment.orbit.EventHandler;

public class AirJump extends Module {
    private final BooleanSetting maintainLevel = new BooleanSetting("Maintain-Level", "Maintains your current Y level when holding the jump key.", false);

    private int level;

    public AirJump() {
        super("Air-Jump", "Lets you jump in the air.", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
        this.addSettings(maintainLevel);
    }

    @Override
    public void onEnable() {
        level = mc.player.getBlockPos().getY();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @HaikuSubscribe
    private void onKey(KeyEvent event) {
        HaikuLogger.info("AirJump.onKey");
        if (Haiku.getInstance().getModuleManager().isModuleEnabled("FreeCam") || mc.currentScreen != null || mc.player.isOnGround()) return;

        if (event.action != KeyAction.Press) return;

        if (mc.options.jumpKey.matchesKey(event.key, 0)) {
            level = mc.player.getBlockPos().getY();
            mc.player.jump();
        }
        else if (mc.options.sneakKey.matchesKey(event.key, 0)) {
            level--;
        }
    }

    @HaikuSubscribe
    private void onTick(TickEvent event) {
        HaikuLogger.info("AirJump.onTick");
        if (Haiku.getInstance().getModuleManager().isModuleEnabled("FreeCam") || mc.player.isOnGround()) return;

        if (maintainLevel.isEnabled() && mc.player.getBlockPos().getY() == level && mc.options.jumpKey.isPressed()) {
            mc.player.jump();
        }
    }
}
