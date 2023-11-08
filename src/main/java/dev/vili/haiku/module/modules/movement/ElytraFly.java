/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.movement;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.glfw.GLFW;
import java.lang.reflect.Field;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import dev.vili.haiku.event.events.entity.player.EventClientMove;
import dev.vili.haiku.event.events.EventPacket;
import dev.vili.haiku.event.events.EventSendMovementPackets;
import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.setting.settings.ModeSetting;
import dev.vili.haiku.setting.settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ElytraFly extends Module {
	public final ModeSetting mode = new ModeSetting("Mode-ElytraFly", "ElytraFly mode (firework can be used with baritone nether elytra with some baritone tweaks).", "Boost", "AutoBoost", "Boost", "Control", "Firework", "BruhFly", "PaketFly");
	public final NumberSetting boostValue = new NumberSetting("Boost-ElytraFly", "Boost speed.", 0.05, 0, 0.25, 0.01);
	public final NumberSetting maxBoost = new NumberSetting("MaxBoost-ElytraFly", "Max boost speed.", 2.5, 0, 8, 0.1);
	public final NumberSetting speed = new NumberSetting("Speed-ElytraFly", "Speed for all the other modes.", 1.2, 0, 10, 0.1);
	public final NumberSetting fireworkLevel = new NumberSetting("FireworkLevel-ElytraFly", "Firework level.", 1, 1, 255, 1);
	public final BooleanSetting playSound = new BooleanSetting("PlaySound-ElytraFly", "Play sound when launching firework using the firework mode.", true);
	public final NumberSetting packets = new NumberSetting("Packets-ElytraFly", "How many packets to send in packet mode.", 0, 0, 8, 0.01);

	private final List<FireworkRocketEntity> fireworks = new ArrayList<>();
	private long timer;

	public ElytraFly() {
		super("ElytraFly", "Improves the elytra", GLFW.GLFW_KEY_H, Category.MOVEMENT);
		this.addSettings(mode, boostValue, maxBoost, fireworkLevel, playSound, speed, packets);
	}

	@Override
	public void onDisable() {
		for (FireworkRocketEntity entity : fireworks) {
			entity.kill();
		}
		fireworks.clear();
		super.onDisable();
	}

	@HaikuSubscribe
	public void onClientMove(EventClientMove event) {
		/* Cancel the retarded auto elytra movement */
		if (mode.getMode() == "Control" && mc.player.isFallFlying()) {
			if (!mc.options.jumpKey.isPressed() && !mc.options.sneakKey.isPressed()) {
				event.setVec(new Vec3d(event.getVec().x, 0, event.getVec().z));
			}

			if (!mc.options.backKey.isPressed() && !mc.options.leftKey.isPressed()
					&& !mc.options.rightKey.isPressed() && !mc.options.forwardKey.isPressed()) {
				event.setVec(new Vec3d(0, event.getVec().y, 0));
			}
		}
	}

	@HaikuSubscribe
	public void onTick(TickEvent event) {
		Vec3d vec3d = new Vec3d(0, 0, speed.getValue());
		vec3d = vec3d.rotateY(-(float) Math.toRadians(mc.player.getYaw()));

		double currentVel = Math.abs(mc.player.getVelocity().x) + Math.abs(mc.player.getVelocity().y) + Math.abs(mc.player.getVelocity().z);
		float radianYaw = (float) Math.toRadians(mc.player.getYaw());
		float boost = (float) boostValue.getValue();

		switch (mode.getMode()) {
			case "AutoBoost":
				if (mc.player.isFallFlying() && currentVel <= maxBoost.getValue()) {
					if (mc.options.backKey.isPressed()) {
						mc.player.addVelocity(MathHelper.sin(radianYaw) * boost, 0, MathHelper.cos(radianYaw) * -boost);
					} else if (mc.player.getPitch() > 0) {
						mc.player.addVelocity(MathHelper.sin(radianYaw) * -boost, 0, MathHelper.cos(radianYaw) * boost);
					}
				}

				break;
			case "Boost":
				if (mc.player.isFallFlying() && currentVel <= maxBoost.getValue()) {
					if (mc.options.forwardKey.isPressed()) {
						mc.player.addVelocity(MathHelper.sin(radianYaw) * -boost, 0, MathHelper.cos(radianYaw) * boost); // (double) getRandInt(0.1, 1)
					} else if (mc.options.backKey.isPressed()) {
						mc.player.addVelocity(MathHelper.sin(radianYaw) * boost, 0, MathHelper.cos(radianYaw) * -boost);
					}
				}

				break;
			case "Control":
				if (mc.player.isFallFlying()) {
					if (mc.options.backKey.isPressed()) vec3d = vec3d.negate();
					if (mc.options.leftKey.isPressed()) vec3d = vec3d.rotateY((float) Math.toRadians(90));
					else if (mc.options.rightKey.isPressed()) vec3d = vec3d.rotateY(-(float) Math.toRadians(90));
					if (mc.options.jumpKey.isPressed()) vec3d = vec3d.add(0, speed.getValue(), 0);
					if (mc.options.sneakKey.isPressed()) vec3d = vec3d.add(0, -speed.getValue(), 0);

					mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
							mc.player.getX() + vec3d.x, mc.player.getY() - 0.01, mc.player.getZ() + vec3d.z, false));

					mc.player.setVelocity(vec3d.x, vec3d.y, vec3d.z);
				}
			
			case "Firework":
				if (mc.player.isFallFlying() && mc.currentScreen == null) {
					ItemStack itemStack = Items.FIREWORK_ROCKET.getDefaultStack();
					itemStack.getOrCreateSubNbt("Fireworks").putByte("Flight", (byte) fireworkLevel.getValue());

					FireworkRocketEntity entity = new FireworkRocketEntity(mc.world, itemStack, mc.player);
					fireworks.add(entity);
					if (playSound.isEnabled()) mc.world.playSoundFromEntity(mc.player, entity, SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F);
					mc.world.spawnEntity(entity);
					timer += 1;
					if (timer > 20) {
						timer = 0;
						for (FireworkRocketEntity fireworkEntity : fireworks) {
							fireworkEntity.kill();
						}
					}
        		}


				break;
			case "BruhFly":
				if (shouldPacketFly()) {
					mc.player.setVelocity(vec3d);
					mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.START_FALL_FLYING));
					mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
							mc.player.getX() + vec3d.x, mc.player.getY() + vec3d.y, mc.player.getZ() + vec3d.z, true));
				}

				break;
			case "PaketFly":
				if (shouldPacketFly()) {
					mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
					double randMult = RandomUtils.nextDouble(0.9, 1.1);

					mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
							mc.player.getX() + vec3d.x * randMult,
							mc.player.getY(),
							mc.player.getZ() + vec3d.z * randMult,
							false));

					for (int i = 0; i < 6; i++) {
						mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
								mc.player.getX() + vec3d.x * (randMult + i),
								mc.player.getY() - 0.0001,
								mc.player.getZ() + vec3d.z * (randMult + i),
								true));
					}
				}
		}
	}

	// Packet moment

	@HaikuSubscribe
	public void onMovement(EventSendMovementPackets event) {
		if (mode.getMode() == "PaketFly" && shouldPacketFly()) {
			mc.player.setVelocity(Vec3d.ZERO);
			event.setCancelled(true);
		}
	}

	@HaikuSubscribe
	public void onMovement(EventClientMove event) {
		if (mode.getMode() == "PaketFly" && shouldPacketFly()) {
			event.setCancelled(true);
		}
	}

		@HaikuSubscribe
		public void onReadPacket(EventPacket.Read event) {
			if (mode.getMode() == "PaketFly" && shouldPacketFly() && event.getPacket() instanceof PlayerPositionLookS2CPacket) {
				PlayerPositionLookS2CPacket p = (PlayerPositionLookS2CPacket) event.getPacket();

				try {
					Field yawField = PlayerPositionLookS2CPacket.class.getDeclaredField("yaw");
					yawField.setAccessible(true);
					yawField.setFloat(p, mc.player.getYaw());

					Field pitchField = PlayerPositionLookS2CPacket.class.getDeclaredField("pitch");
					pitchField.setAccessible(true);
					pitchField.setFloat(p, mc.player.getPitch());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	@HaikuSubscribe
	public void onSendPacket(EventPacket.Send event) {
		if (mode.getMode() == "PaketFly" && shouldPacketFly()) {
			if (event.getPacket() instanceof PlayerMoveC2SPacket.LookAndOnGround) {
				event.setCancelled(true);
				return;
			}

			if (event.getPacket() instanceof PlayerMoveC2SPacket.Full) {
				event.setCancelled(true);
				PlayerMoveC2SPacket p = (PlayerMoveC2SPacket) event.getPacket();
				mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.getX(0), p.getY(0), p.getZ(0), p.isOnGround()));
			}
		}
	}

	private boolean shouldPacketFly() {
		return !mc.player.isOnGround()
				&& !mc.options.sneakKey.isPressed()
				&& mc.player.getInventory().getArmorStack(2).getItem() == Items.ELYTRA;
	}

	private double getRandInt(double d, double max) {
		Random rand = new Random();
		return (double) rand.nextInt((int) (max - d)) + d;
	}
}
