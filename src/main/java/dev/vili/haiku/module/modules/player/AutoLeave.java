/*
 * Copyright (c) 2014-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.player;

import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.Haiku;
import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.setting.settings.ModeSetting;

import org.lwjgl.glfw.GLFW;

public final class AutoLeave extends Module
{
	private final NumberSetting health = new NumberSetting("Health","Leaves the server when your health reaches this value or falls below it.",4.0, 0.5, 9.5, 0.5);
	public final ModeSetting mode = new ModeSetting("Mode", "Select the mode for leaving the server.", "Quit", "Quit", "Chars", "TP", "SelfHurt");
		public AutoLeave()
	{
		super("AutoLeave", "Leaves the server when your health is low.", GLFW.GLFW_KEY_UNKNOWN, Category.PLAYER);
		this.addSettings(health, mode);
	}

	@Override
	public void onEnable()
	{
		mc.world.disconnect();
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
		mc.world.disconnect();
		// check gamemode
		if(mc.player.getAbilities().creativeMode)
			return;
		
		// check for other players
		if(mc.isInSingleplayer()
			&& mc.player.networkHandler.getPlayerList().size() == 1)
			return;
		
		// check health
		if(mc.player.getHealth() > (float) health.getValue() * 2F)
			return;
		
		// leave server
		switch(mode.getMode())
		{
			case "Quit":
			mc.world.disconnect();
			break;
			
			case "Chars":
			mc.getNetworkHandler().sendChatMessage("\u00a7");
			break;
			
			case "TP":
			mc.player.networkHandler
				.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(3.1e7,
					100, 3.1e7, false));
			break;
			
			case "SelfHurt":
			mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket
				.attack(mc.player, mc.player.isSneaking()));
			break;
		}
		
		// disable
		setEnabled(false);
	}
}
