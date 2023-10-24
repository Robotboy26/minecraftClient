package dev.vili.haiku.module.modules.movement;

import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.PacketEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.Setting;
import dev.vili.haiku.setting.settings.NumberSetting;

public class DamageFly extends Module {
    public DamageFly() {
        super("DamageFly", "Fly when you take damage", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
        this.addSettings(boostTicks, hurtTime);
    }

    public final NumberSetting boostTicks = new NumberSetting("Ticks", "How long to boost for", 8, 0, 40, 1);
    public final NumberSetting hurtTime = new NumberSetting("HurtTime", "How long to wait for damage", 9, 1, 10, 1);

    private boolean canBoost, damage, isVelocity;
    private int ticks;
    private double motion;

    @HaikuSubscribe
    public void onPacketReceive(PacketEvent e) {
        if (e.getPacket() instanceof EntityVelocityUpdateS2CPacket velPacket) {
            if (velPacket.getVelocityY() > 0)
                isVelocity = true;

            if (velPacket.getVelocityY() / 8000.0 > 0.2) {
                motion = velPacket.getVelocityY() / 8000.0;
                canBoost = true;
            }
        }
    }

    @HaikuSubscribe
    public void onTick() {
        if (mc.player.hurtTime == hurtTime.getValue())
            damage = true;

        if (damage && isVelocity) {
            if (canBoost) {
                mc.player.setVelocity(mc.player.getVelocity().x, motion, mc.player.getVelocity().getZ());
                ++ticks;
            }
            if (ticks >= boostTicks.getValue()) {
                isVelocity = false;
                canBoost = false;
                damage = false;
                ticks = 0;
            }
        }
    }

    @Override
    public void onEnable() {
        damage = false;
        canBoost = false;
        ticks = 0;
        super.onEnable();
    }
}