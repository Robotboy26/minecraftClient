package dev.vili.haiku.module.modules.player;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.utils.HaikuLogger;
import net.minecraft.text.Text;
public class LogoutTimer extends Module {
    public final NumberSetting logoutTimeSetting = new NumberSetting("LogoutTime-LogoutTimer", "The time in seconds before logging out.", 60, 0, 3600, 1);

    public double logoutTime = 0;

    public LogoutTimer() {
        super("LogoutTimer", "This will log you out after some time(recommended use with AutoLog).", GLFW.GLFW_KEY_UNKNOWN, Category.PLAYER);
        this.addSettings(logoutTimeSetting);
    }

    @Override
    public void onEnable() {
        logoutTime = logoutTimeSetting.getValue() * 20; 
        super.onEnable();
    }

    @Override
    public void onDisable() {
        logoutTime = 0;
        super.onDisable();
    }

    @HaikuSubscribe
    public void onTick(TickEvent event) {
        if (mc.player == null) return;
        if (mc.player.isDead()) return;
        if (logoutTime / 20 == 60) {
            HaikuLogger.info("You will be logged out in 1 minute");
        }

        if (logoutTime != 0) {
            logoutTime -= 1;
            if (logoutTime <= 0) {
                mc.player.networkHandler.getConnection().disconnect(Text.of("LogoutTimer logged you out"));
                super.onDisable();
            }
        }
    }
}
