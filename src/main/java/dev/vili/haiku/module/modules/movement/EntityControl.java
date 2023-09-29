/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.movement;

import dev.vili.haiku.event.events.entity.EventEntityControl;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.PacketEvent;
import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.utils.world.WorldUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemSteerable;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.tick.Tick;

public class EntityControl extends Module {
	public final BooleanSetting entitySpeed = new BooleanSetting("EntitySpeed", "Lets you control the speed of the riding entities", true);
	public final NumberSetting speedOfEntity = new NumberSetting("Speed", "The speed of the entity", 1.2, 0, 5, 0.1);
	public final BooleanSetting entityFly = new BooleanSetting("EntityFly", "Lets you fly with entities", false);
	public final NumberSetting ascend = new NumberSetting("Ascend", "Ascend speed", 0.3, 0, 2, 0.1);
	public final NumberSetting descend = new NumberSetting("Descend", "Descend speed", 0.5, 0, 2, 0.1);
	public final BooleanSetting horseJump = new BooleanSetting("HorseJump", "Makes your horse always do the highest jump it can", true);
	public final BooleanSetting groundSnap = new BooleanSetting("GroundSnap", "Snaps the entity to the ground when going down blocks", false);
	public final BooleanSetting antiStuck = new BooleanSetting("AntiStuck", "Tries to prevent rubberbanding when going up blocks", false);
	public final BooleanSetting noAI = new BooleanSetting("NoAI", "Disables the entities AI", true);
	public final BooleanSetting rotationLock = new BooleanSetting("RotationLock", "Locks the rotation of the vehicle to a certain angle serverside", false);
	public final NumberSetting yawRL = new NumberSetting("Yaw", "Yaw of the vehicle", 0, -180, 180, 1);
	public final NumberSetting pitchRL = new NumberSetting("Pitch", "Pitch of the vehicle", 0, -90, 90, 1);
	public final BooleanSetting playerRL = new BooleanSetting("Player", "Also locks roation for player packets", true);
	public final BooleanSetting antiDismount = new BooleanSetting("AntiDismount", "Prevents you from getting distmounted by the server", false);

	public EntityControl() {
		super("EntityControl", "Manipulates Entities", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
		this.addSettings(entitySpeed, speedOfEntity, entityFly, ascend, descend, horseJump, groundSnap, antiStuck, noAI, rotationLock, yawRL, pitchRL, playerRL, antiDismount);
	}

	@Override
	public void onEnable() {
		if (mc.player == null || mc.player.getVehicle() == null)
			return;

		Entity e = mc.player.getVehicle();
		e.setVelocity(0, 0, 0);
		e.fallDistance = 0;
		super.onEnable();
	}

	@Override
	public void onDisable() {
		if (mc.player == null || mc.player.getVehicle() == null)
			return;

		Entity e = mc.player.getVehicle();
		e.setVelocity(0, 0, 0);
		e.fallDistance = 0;
		super.onDisable();
	}

	@HaikuSubscribe
	public void onTick(TickEvent event) {
		if (mc.player.getVehicle() == null)
			return;

		Entity e = mc.player.getVehicle();
		double speed = speedOfEntity.getValue();

		double forward = mc.player.forwardSpeed;
		double strafe = mc.player.sidewaysSpeed;
		float yaw = mc.player.getYaw();

		e.setYaw(yaw);
		if (e instanceof LlamaEntity) {
			((LlamaEntity) e).headYaw = mc.player.headYaw;
		}

		if (noAI.isEnabled() && forward == 0 && strafe == 0) {
			e.setVelocity(new Vec3d(0, e.getVelocity().y, 0));
		}

		if (entitySpeed.isEnabled()) {
			if (forward != 0.0D) {
				if (strafe > 0.0D) {
					yaw += (forward > 0.0D ? -45 : 45);
				} else if (strafe < 0.0D) {
					yaw += (forward > 0.0D ? 45 : -45);
				}

				if (forward > 0.0D) {
					forward = 1.0D;
				} else if (forward < 0.0D) {
					forward = -1.0D;
				}

				strafe = 0.0D;
			}

			e.setVelocity(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)),
					e.getVelocity().y,
					forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
		}

		if (entityFly.isEnabled()) {
			if (mc.options.jumpKey.isPressed()) {
				e.setVelocity(e.getVelocity().x, ascend.getValue(), e.getVelocity().z);
			} else {
				e.setVelocity(e.getVelocity().x, -descend.getValue(), e.getVelocity().z);
			}
		}

		if (groundSnap.isEnabled()) {
			BlockPos p = BlockPos.ofFloored(e.getPos());
			if (e.fallDistance > 0.01) {
				e.setVelocity(e.getVelocity().x, -1, e.getVelocity().z);
			}
		}

		if (horseJump.isEnabled()) {
			Vec3d vel = e.getVelocity().multiply(2);
			if (WorldUtils.doesBoxCollide(e.getBoundingBox().offset(vel.x, 0, vel.z))) {
				for (int i = 2; i < 10; i++) {
					if (!WorldUtils.doesBoxCollide(e.getBoundingBox().offset(vel.x / i, 0, vel.z / i))) {
						e.setVelocity(vel.x / i / 2, vel.y, vel.z / i / 2);
						break;
					}
				}
			}
		}
	}

	// @HaikuSubscribe
	// public void onSendPacket(PacketEvent event) {
	// 	if (rotationLock.isEnabled()) {
	// 		if (event.getPacket() instanceof VehicleMoveC2SPacket) {
	// 			VehicleMoveC2SPacket packet = (VehicleMoveC2SPacket) event.getPacket();
	// 			packet.yaw = (float) yawRL.getValue();
	// 			packet.pitch = (float) pitchRL.getValue();
	// 		} else if (event.getPacket() instanceof PlayerMoveC2SPacket
	// 				&& mc.player.hasVehicle()
	// 				&& playerRL.isEnabled()) {
	// 			PlayerMoveC2SPacket packet = (PlayerMoveC2SPacket) event.getPacket();
	// 			packet.yaw = (float) yawRL.getValue();
	// 			packet.pitch = (float) pitchRL.getValue();
	// 		}
	// 	}

	// 	if (antiDismount.isEnabled() && event.getPacket() instanceof VehicleMoveC2SPacket && mc.player.hasVehicle()) {
	// 		mc.interactionManager.interactEntity(mc.player, mc.player.getVehicle(), Hand.MAIN_HAND);
	// 	}
	// }

	@HaikuSubscribe
	public void onReadPacket(PacketEvent event) {
		if (antiDismount.isEnabled() && mc.player != null && mc.player.hasVehicle() && !mc.player.input.sneaking
				&& (event.getPacket() instanceof PlayerPositionLookS2CPacket || event.getPacket() instanceof EntityPassengersSetS2CPacket)) {
			event.setCancelled(true);
		}
	}

	@HaikuSubscribe
	public void onEntityControl(EventEntityControl event) {
		if (mc.player.getVehicle() instanceof ItemSteerable && mc.player.forwardSpeed == 0 && mc.player.sidewaysSpeed == 0) {
			return;
		}

		event.setControllable(true);
	}

	// HorseJump handled in MixinClientPlayerEntity.method_3151
}
