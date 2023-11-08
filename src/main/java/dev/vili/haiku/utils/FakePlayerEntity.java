/*
 * Copyright (c) 2014-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.utils;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import dev.vili.haiku.Haiku;

public class FakePlayerEntity extends OtherClientPlayerEntity
{
	private final ClientPlayerEntity player = Haiku.mc.player;
	private final ClientWorld world = Haiku.mc.world;
	
	public FakePlayerEntity()
	{
		super(Haiku.mc.world, Haiku.mc.player.getGameProfile());
		copyPositionAndRotation(player);
		
		copyInventory();
		copyPlayerModel(player, this);
		copyRotation();
		resetCapeMovement();
		
		spawn();
	}
	
	private void copyInventory()
	{
		getInventory().clone(player.getInventory());
	}
	
	private void copyPlayerModel(Entity from, Entity to)
	{
		DataTracker fromTracker = from.getDataTracker();
		DataTracker toTracker = to.getDataTracker();
		Byte playerModel = fromTracker.get(PlayerEntity.PLAYER_MODEL_PARTS);
		toTracker.set(PlayerEntity.PLAYER_MODEL_PARTS, playerModel);
	}
	
	private void copyRotation()
	{
		headYaw = player.headYaw;
		bodyYaw = player.bodyYaw;
	}
	
	private void resetCapeMovement()
	{
		capeX = getX();
		capeY = getY();
		capeZ = getZ();
	}
	

	private void spawn()
	{
		world.spawnEntity(this);
	}

	public void despawn()
	{
		discard();
	}
	
	public void resetPlayerPosition()
	{
		player.refreshPositionAndAngles(getX(), getY(), getZ(), getYaw(),
			getPitch());
	}
}
