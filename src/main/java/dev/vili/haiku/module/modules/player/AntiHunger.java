/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.module.modules.player;

import dev.vili.haiku.event.events.packets.PacketEvent;
import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import dev.vili.haiku.mixinterface.PlayerMoveC2SPacketAccessor;

import org.lwjgl.glfw.GLFW;

public class AntiHunger extends Module 
{
    public final BooleanSetting sprint = new BooleanSetting("sprint-AntiHunger", "Spoofs sprinting packets.", true);
    public final BooleanSetting onGround = new BooleanSetting("on-ground-AntiHunger", "Spoofs the onGround flag.", true);
    public final BooleanSetting waterCheck = new BooleanSetting("water-check-AntiHunger", "Pauses the module if you are in water", true);

    private boolean lastOnGround;
    private boolean sendOnGroundTruePacket;
    private boolean ignorePacket;

    public AntiHunger() {
        super("Anti-Hunger", "This prevents you from getting low on food", GLFW.GLFW_KEY_UNKNOWN, Category.PLAYER);
        this.addSettings(sprint, onGround, waterCheck);
    }

    @Override
    public void onEnable() {
        lastOnGround = mc.player.isOnGround();
        sendOnGroundTruePacket = true;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        ignorePacket = false;
        super.onDisable();
    }

    @HaikuSubscribe
    private void onSendPacket(PacketEvent.Send event) {
        if(!(event.getPacket() instanceof PlayerMoveC2SPacket oldPacket))
			return;
		
		if(!mc.player.isOnGround() || mc.player.fallDistance > 0.5)
			return;
		
		if(mc.interactionManager.isBreakingBlock())
			return;
		
		double x = oldPacket.getX(-1);
		double y = oldPacket.getY(-1);
		double z = oldPacket.getZ(-1);
		float yaw = oldPacket.getYaw(-1);
		float pitch = oldPacket.getPitch(-1);
		
		Packet<?> newPacket;
		if(oldPacket.changesPosition())
			if(oldPacket.changesLook())
				newPacket =
					new PlayerMoveC2SPacket.Full(x, y, z, yaw, pitch, false);
			else
				newPacket =
					new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false);
		else if(oldPacket.changesLook())
			newPacket =
				new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, false);
		else
			newPacket = new PlayerMoveC2SPacket.OnGroundOnly(false);
		
		event.setPacket(newPacket);
    }

    @HaikuSubscribe
    private void onTick(TickEvent event) {
        if (waterCheck.isEnabled() && mc.player.isTouchingWater()) {
            ignorePacket = true;
            return;
        }
        if (mc.player.isOnGround() && !lastOnGround && !sendOnGroundTruePacket) sendOnGroundTruePacket = true;

        if (mc.player.isOnGround() && sendOnGroundTruePacket && onGround.isEnabled()) {
            ignorePacket = true;
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
            ignorePacket = false;

            sendOnGroundTruePacket = false;
        }

        lastOnGround = mc.player.isOnGround();
    }
}
