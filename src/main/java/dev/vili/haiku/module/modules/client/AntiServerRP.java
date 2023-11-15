package dev.vili.haiku.module.modules.client;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.PacketEvent;
import dev.vili.haiku.module.Module;

public final class AntiServerRP extends Module {
    private static AntiServerRP instance;

    public AntiServerRP() {
        super("AntiServerRP", "Automatically accepts server resource packs", GLFW.GLFW_KEY_UNKNOWN, Category.CLIENT);
        instance = this;
    }

    @EventHandler
    public void onPacketReceive(PacketEvent e) { 
        if (e.getPacket() instanceof ResourcePackSendS2CPacket) {
            sendPacket(new ResourcePackStatusC2SPacket(ResourcePackStatusC2SPacket.Status.ACCEPTED));
            e.cancel();
        }
    }

    public static AntiServerRP getInstance() {
        return instance;
    }
}
