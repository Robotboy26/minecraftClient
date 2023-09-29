/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.combat;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.ModeSetting;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.utils.world.EntityUtils;
import dev.vili.haiku.utils.world.WorldUtils;

import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;

import com.google.common.collect.Streams;

import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Killaura extends Module {
	public final ModeSetting mode = new ModeSetting("Mode-KillAura", "How to sort targets.", "Sort", "Sort", "Angle", "Distance");
	public final BooleanSetting players = new BooleanSetting("Players-KillAura", "Attacks Players.", true);
	public final BooleanSetting mobs = new BooleanSetting("Mobs-KillAura", "Attacks Mobs.", true);
	public final BooleanSetting animals = new BooleanSetting("Animals-KillAura", "Attacks Animals.", false);
	public final BooleanSetting armorStands = new BooleanSetting("ArmorStands-KillAura", "Attacks Armor Stands.", false);
	public final BooleanSetting projectiles = new BooleanSetting("Projectiles-KillAura", "Attacks Shulker Bullets & Fireballs.", false);
	public final BooleanSetting triggerbot = new BooleanSetting("Triggerbot-KillAura", "Only attacks the entity you're looking at.", false);
	public final BooleanSetting multiAura = new BooleanSetting("MultiAura-KillAura", "Atacks multiple entities at once.", false);
	public final NumberSetting targets = new NumberSetting("Targets-MultiAura", "How many targets to attack at once.", 3, 1, 20, 0);
	public final BooleanSetting rotate = new BooleanSetting("Rotate-KillAura", "Rotates when attackign entities.", true);
	public final BooleanSetting raycast = new BooleanSetting("Raycast-KillAura", "Only attacks if you can see the target.", true);
	public final BooleanSetting delayKillAura = new BooleanSetting("1.9 Delay-KillAura", "Uses the 1.9+ delay between hits.", true);
	public final NumberSetting range = new NumberSetting("Range-KillAura", "Attack range.", 4.25, 0, 6, 2);
	public final NumberSetting cps = new NumberSetting("CPS-KillAura", "Attack CPS if 1.9 delay is disabled.", 8, 0, 40, 1);

	private int delay = 0;

	public Killaura() {
		super("Killaura", "Automatically attacks entities.", GLFW.GLFW_KEY_UNKNOWN, Category.COMBAT);
		this.addSettings(mode, players, mobs, animals, armorStands, projectiles, triggerbot, multiAura, targets, rotate, raycast, delayKillAura, range, cps);
	}

	@HaikuSubscribe
	public void onTick(TickEvent event) {
		if (!mc.player.isAlive()) {
			return;
		}

		delay++;
		int reqDelay = (int) Math.rint(20 / cps.getValue());

		boolean cooldownDone = delayKillAura.isEnabled()
				? mc.player.getAttackCooldownProgress(mc.getTickDelta()) == 1.0f
				: (delay > reqDelay || reqDelay == 0);

		if (cooldownDone) {
			for (Entity e: getEntities()) {
				boolean shouldRotate = DebugRenderer.getTargetedEntity(mc.player, 7).orElse(null) != e;

				if (shouldRotate) {
					WorldUtils.facePos(e.getX(), e.getY() + e.getHeight() / 2, e.getZ());
				}

				boolean wasSprinting = mc.player.isSprinting();

				if (wasSprinting)
					mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.STOP_SPRINTING));

				mc.interactionManager.attackEntity(mc.player, e);
				mc.player.swingHand(Hand.MAIN_HAND);

				if (wasSprinting)
					mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.START_SPRINTING));

				delay = 0;
			}
		}
	}

	private List<Entity> getEntities() {
		Stream<Entity> targets;

		if (triggerbot.isEnabled()) {
			Optional<Entity> entity = DebugRenderer.getTargetedEntity(mc.player, 7);

			if (entity.isEmpty()) {
				return Collections.emptyList();
			}

			targets = Stream.of(entity.get());
		} else {
			targets = Streams.stream(mc.world.getEntities());
		}

		Comparator<Entity> comparator;

		if (mode.getMode() == "Sort") {
			comparator = Comparator.comparing(e -> {
				Vec3d center = e.getBoundingBox().getCenter();

				double diffX = center.x - mc.player.getX();
				double diffY = center.y - mc.player.getEyeY();
				double diffZ = center.z - mc.player.getZ();

				double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

				float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
				float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

				return Math.abs(MathHelper.wrapDegrees(yaw - mc.player.getYaw())) + Math.abs(MathHelper.wrapDegrees(pitch - mc.player.getPitch()));
			});
		} else {
			comparator = Comparator.comparing(mc.player::distanceTo);
		}

		return targets
				.filter(e -> EntityUtils.isAttackable(e, true)
					&& mc.player.distanceTo(e) <= range.getValue()
					&& (mc.player.canSee(e) || !raycast.isEnabled()))
				.filter(e -> EntityUtils.isAttackable(e, true) && e instanceof LivingEntity)
				.sorted(comparator)
				.limit(multiAura.isEnabled() ? targets.count() : 1)
				.map(Entity.class::cast)
				.collect(Collectors.toList());
				}
		}
