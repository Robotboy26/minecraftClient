/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.player;

import dev.vili.haiku.event.events.EventReach;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.NumberSetting;

import org.lwjgl.glfw.GLFW;

public class Reach extends Module {
	public final NumberSetting reach = new NumberSetting("Reach-Reach", "How much further to be able to reach.", 0.3, 0, 5, 0.1);

	public Reach() {
		super("Reach", "Turns you into long armed popbob.", GLFW.GLFW_KEY_UNKNOWN, Category.PLAYER);
		this.addSettings(reach);
	}

	@HaikuSubscribe
	public void onReach(EventReach event) {
		event.setReach(event.getReach() + (float) reach.getValue());
	}
}
