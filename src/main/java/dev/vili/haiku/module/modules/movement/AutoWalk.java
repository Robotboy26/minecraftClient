/*
 * Copyright (c) 2014-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.vili.haiku.module.modules.movement;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.setting.settings.NumberSetting;
import org.lwjgl.glfw.GLFW;

public class AutoWalk extends Module
{
	public AutoWalk()
	{
		super("AutoWalk", "Walk but auto?", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
	}
	
	@Override
	public void onDisable()
	{
		if (mc.world == null || mc.player == null) return;
		mc.options.forwardKey.setPressed(false);
		super.onDisable();
	}
	
	@HaikuSubscribe
	public void onTick(TickEvent event)
	{
		mc.options.forwardKey.setPressed(true);
	}
}
