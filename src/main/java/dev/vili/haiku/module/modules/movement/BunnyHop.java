/*
 * Copyright (c) 2014-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.movement;

import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;
import net.minecraft.client.network.ClientPlayerEntity;

import org.lwjgl.glfw.GLFW;


public final class BunnyHop extends Module
{
	private final BooleanSetting jumpIfSprinting = new BooleanSetting("Jump if Sprinting-BunnyHop", "description", true);
	private final BooleanSetting jumpIfWalking = new BooleanSetting("Jump if Walking-BunnyHop", "description", false);
	private final BooleanSetting jumpIfAlways = new BooleanSetting("Jump Always-BunnyHop", "description", false);
	
	public BunnyHop()
	{
		super("BunnyHop", "Automatically jumps when you move.", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
		this.addSettings(jumpIfSprinting, jumpIfWalking, jumpIfAlways);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
	}
	
	@Override
	public void onDisable()
	{
		super.onDisable();
	}
	
	@HaikuSubscribe
	public void onTick()
	{
		ClientPlayerEntity player = mc.player;
		if(!player.isOnGround() || player.isSneaking())
			return;
		
		if(jumpIfSprinting.isEnabled() && player.isSprinting() && (player.forwardSpeed != 0 || player.sidewaysSpeed != 0))
			player.jump();
		else if(jumpIfWalking.isEnabled() && !player.isSprinting() && (player.forwardSpeed != 0 || player.sidewaysSpeed != 0))
			player.jump();
		else if(jumpIfAlways.isEnabled())
			player.jump();
	}
}
