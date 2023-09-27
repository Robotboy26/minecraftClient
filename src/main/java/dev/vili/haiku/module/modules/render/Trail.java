/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.module.modules.render;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.ModeSetting;
import net.minecraft.particle.ParticleTypes;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.setting.settings.BooleanSetting;

import org.lwjgl.glfw.GLFW;

public class Trail extends Module
{
    public final ModeSetting particles = new ModeSetting("Particles", "The particles to use.", "Smoke", "Dripping Obsidian Tear Smoke");
    public final BooleanSetting pause = new BooleanSetting("Pause-when-stationary", "Whether or not to add particles when you are not moving.", true);

    public Trail() {
		super("Trail", "Renders a customizable trail behind your player.", GLFW.GLFW_KEY_UNKNOWN, Category.RENDER);
        this.addSettings(particles, pause);
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
        if (pause.isEnabled()
            && mc.player.getX() == mc.player.prevX
            && mc.player.getY() == mc.player.prevY
            && mc.player.getZ() == mc.player.prevZ) return;

        if (particles.getMode() == "Dripping Obsidian Tear") {
            mc.world.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, mc.player.getX(), mc.player.getY(), mc.player.getZ(), 0, 0, 0);
        } else if (particles.getMode() == "Smoke") {
            mc.world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, mc.player.getX(), mc.player.getY(), mc.player.getZ(), 0, 0, 0);
        }
    }
}
