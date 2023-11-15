package dev.vili.haiku.module.modules.client;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import baritone.api.event.events.PacketEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.*;

public final class Media extends Module {
    public static final BooleanSetting skinProtect = new BooleanSetting("Skin Protect", "Skin Protect", false);
    public static final BooleanSetting nickProtect = new BooleanSetting("Nick Protect", "Nick Protect", false);

    private static Media instance;

    public Media() {
        super("Media", "Media", GLFW.GLFW_KEY_UNKNOWN, Category.CLIENT);
        instance = this;
    }

    // @HaikuSubscribe
    // public void onPacketReceive(PacketEvent e) {
    //     if (e instanceof GameMessageS2CPacket pac && nickProtect.getValue()) {
    //         for (PlayerListEntry ple : mc.player.networkHandler.getPlayerList()) {
    //             if (pac.content().getString().contains(ple.getProfile().getName())) {
    //                 IGameMessageS2CPacket packet = e.getPacket();
    //                 packet.setContent(Text.of(pac.content().getString().replace(ple.getProfile().getName(), "Protected")));
    //             }
    //         }
    //     }
    // }

    public static Media getInstance() {
        return instance;
    }
}
