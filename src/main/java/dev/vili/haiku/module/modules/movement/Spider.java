/*
 * Copyright (c) 2014-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;


public class Spider extends Module
{
	public Spider()
	{
		super("Spider", "climb walls", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
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
    public void onTick(TickEvent event) 
	{
		ClientPlayerEntity player = mc.player;
		if(!player.horizontalCollision)
			return;
		
		Vec3d velocity = player.getVelocity();
		if(velocity.y >= 0.2)
			return;
		
		player.setVelocity(velocity.x, 0.2, velocity.z);
	}
}
