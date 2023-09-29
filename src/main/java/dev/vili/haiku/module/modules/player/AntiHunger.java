/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.player;

import java.lang.reflect.Field;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.EventPacket;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class AntiHunger extends Module {
	public final BooleanSetting relaxed = new BooleanSetting("Relaxed", "Only activate every other tick, might fix getting fly kicked", false);

	private boolean bool = false;

	public AntiHunger() {
		super("AntiHunger", "Minimizes the amount of hunger you use (Also makes you slide).", GLFW.GLFW_KEY_UNKNOWN, Category.PLAYER);
		this.addSettings(relaxed);
	}

		@HaikuSubscribe
		public void onSendPacket(EventPacket.Send event) {
			if (event.getPacket() instanceof PlayerMoveC2SPacket) {
				if (mc.player.getVelocity().y != 0 && !mc.options.jumpKey.isPressed() && (!bool || !relaxed.isEnabled())) {
					boolean onGround = mc.player.fallDistance >= 0.1f;
					mc.player.setOnGround(onGround);
					try {
						Field onGroundField = PlayerMoveC2SPacket.class.getDeclaredField("onGround");
						onGroundField.setAccessible(true);
						onGroundField.setBoolean(event.getPacket(), onGround);
					} catch (NoSuchFieldException | IllegalAccessException e) {
						e.printStackTrace();
					}
					bool = true;
				} else {
					bool = false;
				}
			}
		}
	}
