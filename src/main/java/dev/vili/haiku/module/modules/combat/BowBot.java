/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.combat;

import java.util.Comparator;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.utils.world.EntityUtils;
import dev.vili.haiku.utils.world.WorldUtils;

import com.google.common.collect.Streams;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.tick.Tick;

public class BowBot extends Module {
	public final BooleanSetting shoot = new BooleanSetting("Shoot-BowBot", "Automatically shoots arrows.", true);
	public final NumberSetting charge = new NumberSetting("Charge-BowBot", "How much to charge the bow before shooting.", .75, .5, 1, 0.01);
	public final BooleanSetting aim = new BooleanSetting("Aim-BowBot", "Automatically aims.", true);
	public final BooleanSetting players = new BooleanSetting("Players-BowBot", "Aims at players.", true);
	public final BooleanSetting mobs = new BooleanSetting("Mobs-BowBot", "Aims at mobs.", false);
	public final BooleanSetting animals = new BooleanSetting("Animals-BowBot", "Aims at animals.", false);
	public final BooleanSetting raycast = new BooleanSetting("Raycast-BowBot", "Doesn't aim at entites you can't see.", false);

	public BowBot() {
		super("BowBot", "Automatically aims and shoots at entities.", GLFW.GLFW_KEY_UNKNOWN, Category.COMBAT);
		this.addSettings(shoot, charge, aim, players, mobs, animals, raycast);
	}

	@HaikuSubscribe
	public void onTick(TickEvent event) {
		if (!(mc.player.getMainHandStack().getItem() instanceof RangedWeaponItem) || !mc.player.isUsingItem())
			return;

		if (shoot.isEnabled()) {
			if (mc.player.getMainHandStack().getItem() == Items.CROSSBOW
					&& (float) mc.player.getItemUseTime() / (float) CrossbowItem.getPullTime(mc.player.getMainHandStack()) >= 1f) {
				mc.player.stopUsingItem();
				mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.UP));
				mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
			} else if (mc.player.getMainHandStack().getItem() == Items.BOW
					&& BowItem.getPullProgress(mc.player.getItemUseTime()) >= charge.getValue()) {
				mc.player.stopUsingItem();
				mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.UP));
			}
		}

		// Credit: https://github.com/Wurst-Imperium/Wurst7/blob/master/src/main/java/net/wurstclient/hacks/BowAimbotHack.java
		if (aim.isEnabled()) {
			LivingEntity target = Streams.stream(mc.world.getEntities())
					.filter(e -> EntityUtils.isAttackable(e, true)
							&& (!raycast.isEnabled() || mc.player.canSee(e)))
					.filter(e -> (players.isEnabled() && EntityUtils.isPlayer(e))
							|| (mobs.isEnabled() && EntityUtils.isMob(e))
							|| (animals.isEnabled() && EntityUtils.isAnimal(e)))
					.sorted(Comparator.comparing(mc.player::distanceTo))
					.map(e -> (LivingEntity) e)
					.findFirst().orElse(null);

			if (target == null)
				return;

			// set velocity
			float velocity = (72000 - mc.player.getItemUseTimeLeft()) / 20F;
			velocity = Math.min(1f, (velocity * velocity + velocity * 2) / 3);

			// set position to aim at
			Vec3d newTargetVec = target.getPos().add(target.getVelocity());
			double d = mc.player.getEyePos().distanceTo(target.getBoundingBox().offset(target.getVelocity()).getCenter());
			double x = newTargetVec.x + (newTargetVec.x - target.getX()) * d - mc.player.getX();
			double y = newTargetVec.y + (newTargetVec.y - target.getY()) * d + target.getHeight() * 0.5 - mc.player.getY() - mc.player.getEyeHeight(mc.player.getPose());
			double z = newTargetVec.z + (newTargetVec.z - target.getZ()) * d - mc.player.getZ();

			// set yaw
			mc.player.setYaw((float) Math.toDegrees(Math.atan2(z, x)) - 90);

			// calculate needed pitch
			double hDistance = Math.sqrt(x * x + z * z);
			double hDistanceSq = hDistance * hDistance;
			float g = 0.006F;
			float velocitySq = velocity * velocity;
			float velocityPow4 = velocitySq * velocitySq;
			float neededPitch = (float) -Math.toDegrees(Math.atan((velocitySq - Math
					.sqrt(velocityPow4 - g * (g * hDistanceSq + 2 * y * velocitySq)))
					/ (g * hDistance)));

			// set pitch
			if (Float.isNaN(neededPitch)) {
				WorldUtils.facePos(target.getX(), target.getY() + target.getHeight() / 2, target.getZ());
			} else {
				mc.player.setPitch(neededPitch);
			}
		}
	}
}
