/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.render;

import dev.vili.haiku.event.events.entity.player.EventClientMove;
import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.EventOpenScreen;
import dev.vili.haiku.event.events.PacketEvent;
import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.utils.FakePlayerEntity;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Freecam extends Module {
	public final NumberSetting speed = new NumberSetting("speed", "Your speed while in freecam.", 1.0, 0.0, 20.0, 0.01);
	
	private FakePlayerEntity dummy;
	private double[] playerPos;
	private float[] playerRot;
	private Entity riding;

	private boolean prevFlying;
	private float prevFlySpeed;

    public Freecam() {
        super("Freecam", "See things anywhere", GLFW.GLFW_KEY_G, Category.RENDER);
        super.addSettings(speed);
    }

    @Override
	public void onEnable() {
		mc.chunkCullingEnabled = false;

		playerPos = new double[] { mc.player.getX(), mc.player.getY(), mc.player.getZ() };
		playerRot = new float[] { mc.player.getYaw(), mc.player.getPitch() };

		dummy = new FakePlayerEntity();
        dummy.despawn();
        dummy = new FakePlayerEntity();

		if (mc.player.getVehicle() != null) {
			riding = mc.player.getVehicle();
			mc.player.getVehicle().removeAllPassengers();
		}

		if (mc.player.isSprinting()) {
			mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.STOP_SPRINTING));
		}

		prevFlying = mc.player.getAbilities().flying;
		prevFlySpeed = mc.player.getAbilities().getFlySpeed();
        super.onEnable();
	}

	@Override
	public void onDisable() {
        mc.chunkCullingEnabled = true;

        try {
            dummy.despawn();
        } catch (NullPointerException e) {}
        mc.player.noClip = false;
        mc.player.getAbilities().flying = prevFlying;
        mc.player.getAbilities().setFlySpeed(prevFlySpeed);

        mc.player.refreshPositionAndAngles(playerPos[0], playerPos[1], playerPos[2], playerRot[0], playerRot[1]);
        mc.player.setVelocity(Vec3d.ZERO);

        if (riding != null && mc.world.getEntityById(riding.getId()) != null) {
            mc.player.startRiding(riding);
        }

	super.onDisable();
	}

	@HaikuSubscribe
	public void sendPacket(PacketEvent event) {
		if (event.getPacket() instanceof ClientCommandC2SPacket || event.getPacket() instanceof PlayerMoveC2SPacket) {
			event.setCancelled(true);
		}
	}

	@HaikuSubscribe
	public void onOpenScreen(EventOpenScreen event) {
		if (riding instanceof AbstractHorseEntity) {
			if (event.getScreen() instanceof InventoryScreen) {
				mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.OPEN_INVENTORY));
				event.setCancelled(true);
			}
		}
	}

	@HaikuSubscribe
	public void onClientMove(EventClientMove event) {
		mc.player.noClip = true;
	}

	@HaikuSubscribe
	public void onTick(TickEvent event) {
		mc.player.setOnGround(false);
		mc.player.getAbilities().setFlySpeed((float) (speed.getValue()));
		mc.player.getAbilities().flying = true;
		mc.player.setPose(EntityPose.STANDING);
        mc.player.setVelocity(Vec3d.ZERO);
	}
}