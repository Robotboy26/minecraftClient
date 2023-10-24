/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.module.modules.movement;

import dev.vili.haiku.event.events.packets.PacketEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.utils.Utils;
import dev.vili.haiku.utils.FakePlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.joml.Vector3d;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Blink extends Module {
    public final BooleanSetting renderOriginal = new BooleanSetting("RenderOriginal-Blink", "Renders your player model at the original position.", true);

    private final List<PlayerMoveC2SPacket> packets = new ArrayList<>();
    private FakePlayerEntity model;
    private final Vector3d start = new Vector3d();

    private boolean cancelled = false;
    private int timer = 0;

    public Blink() {
        super("Blink", "Allows you to essentially teleport while suspending motion updates.", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
        this.addSettings(renderOriginal);
    }

    @Override
    public void onEnable() {
        if (renderOriginal.isEnabled()) {
            model = new FakePlayerEntity();
            model.copyPositionAndRotation(mc.player);
            super.onEnable();
        }

        Utils.set(start, mc.player.getPos());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        dumpPackets(!cancelled);
        if (cancelled) mc.player.setPos(start.x, start.y, start.z);
        cancelled = false;
    }

    @HaikuSubscribe
    private void onTick(TickEvent event) {
        timer++;
    }

    @HaikuSubscribe
    private void onSendPacket(PacketEvent.Send event) {
        if (!(event.packet instanceof PlayerMoveC2SPacket p)) return;
        event.setCancelled(true);

        PlayerMoveC2SPacket prev = packets.size() == 0 ? null : packets.get(packets.size() - 1);

        if (prev != null &&
                p.isOnGround() == prev.isOnGround() &&
                p.getYaw(-1) == prev.getYaw(-1) &&
                p.getPitch(-1) == prev.getPitch(-1) &&
                p.getX(-1) == prev.getX(-1) &&
                p.getY(-1) == prev.getY(-1) &&
                p.getZ(-1) == prev.getZ(-1)
        ) return;

        synchronized (packets) {
            packets.add(p);
        }
    }

    private void dumpPackets(boolean send) {
        synchronized (packets) {
            if (send) packets.forEach(mc.player.networkHandler::sendPacket);
            packets.clear();
        }

        if (model != null) {
            model.despawn();
            model = null;
        }

        timer = 0;
    }
}
