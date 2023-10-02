/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.misc;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.EventBlockBreakCooldown;
import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.ModeSetting;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.setting.settings.BooleanSetting;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class FastBreak extends Module {
    public final ModeSetting mode = new ModeSetting("Mode-FastBreak", "FastBreak mode.", "Haste", "Haste", "OG");
    public final NumberSetting hasteLvl = new NumberSetting("HasteLvl-FastBreak", "Haste level.", 1, 1, 3, 1);
    public final NumberSetting cooldown = new NumberSetting("Cooldown-FastBreak", "Cooldown between mining blocks (in ticks).", 0, 0, 4, 1);
    public final NumberSetting multiplier = new NumberSetting("Multiplier-FastBreak", "OG Mode multiplier.", 1.3, 1, 3, 0.1);
    public final BooleanSetting antiFatigue = new BooleanSetting("AntiFatigue-FastBreak", "Removes the fatigue effect.", true);
    public final BooleanSetting antiOffGround = new BooleanSetting("AntiOffGround-FastBreak", "Removing mining slowness from being offground.", true);

	public FastBreak() {
        super("FastBreak", "Allows you to break blocks faster.", GLFW.GLFW_KEY_UNKNOWN, Category.MISC);
        this.addSettings(mode, hasteLvl, cooldown, multiplier, antiFatigue, antiOffGround);
	}

	@Override
	public void onDisable() {
        mc.player.removeStatusEffect(StatusEffects.HASTE);
		super.onDisable();
	}

	@HaikuSubscribe
	public void onTick(TickEvent event) {
		if (mode.getMode() == "Haste") {
			mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 1, (int) hasteLvl.getValue() - 1));
		}
	}

	@HaikuSubscribe
	public void onBlockBreakCooldown(EventBlockBreakCooldown event) {
		event.setCooldown((int) cooldown.getValue());
	}
}
