package dev.vili.haiku.module.modules.client;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.PacketEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.*;
import dev.vili.haiku.utils.thunderClient.utility.Timer;
import dev.vili.haiku.utils.thunderClient.utility.math.MathUtility;

public final class FastLatency extends Module {
    private final NumberSetting delay = new NumberSetting("Delay", "Delay", 1000, 0, 10000, 100);

    public static FastLatency instance;

    private final Timer timer = new Timer();
    private final Timer limitTimer = new Timer();
    private long ping;
    public int resolvedPing;

    public FastLatency() {
        super("FastLatency", "FastLatency", GLFW.GLFW_KEY_UNKNOWN, Category.CLIENT);
        instance = this;
    }

    // @Override
    // public void onRender3D(MatrixStack stack) {
    //     if (timer.passedMs(5000) && limitTimer.every(delay.getValue())) {
    //         sendPacket(new RequestCommandCompletionsC2SPacket(1337, "w "));
    //         ping = System.currentTimeMillis();
    //         timer.reset();
    //     }
    // }

    @HaikuSubscribe
    public void onPacketReceive(PacketEvent e) {
        if (e.getPacket() instanceof CommandSuggestionsS2CPacket c && c.getCompletionId() == 1337) {
            resolvedPing = (int) MathUtility.clamp(System.currentTimeMillis() - ping, 0, 1000);
            timer.setMs(5000);
        }
    }

    public static FastLatency getInstance() {
        return instance;
    }
}
