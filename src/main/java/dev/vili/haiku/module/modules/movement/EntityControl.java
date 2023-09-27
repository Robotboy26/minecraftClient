/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.module.modules.movement;

//Created by squidoodly 10/07/2020

import dev.vili.haiku.mixin.ClientPlayerEntityAccessor;
import dev.vili.haiku.mixinterface.IHorseBaseEntity;
import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;

import org.lwjgl.glfw.GLFW;

public class EntityControl extends Module 
{
    public final BooleanSetting maxJump = new BooleanSetting("Max Jump-EntityControl", "Sets jump power to maximum.", true);

    public EntityControl() {
        super("EntityControl", "Lets you control rideable entities without a saddle.", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @HaikuSubscribe
    private void onTick(TickEvent event) {
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof AbstractHorseEntity) ((IHorseBaseEntity) entity).setSaddled(true);
        }

        if (maxJump.isEnabled()) ((ClientPlayerEntityAccessor) mc.player).setMountJumpStrength(1);
    }
}
