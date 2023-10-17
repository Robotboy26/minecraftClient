package dev.vili.haiku.module.modules.render;

import org.lwjgl.glfw.GLFW;
import dev.vili.haiku.event.events.render.Render3DEvent;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.utils.HaikuLogger;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventBus;
import dev.vili.haiku.Haiku;
import java.util.Collections;

public class NoWeather extends Module {
    public boolean NoWeather = false;
    public NoWeather() {
        super("NoWeather", "NoWeather", GLFW.GLFW_KEY_UNKNOWN, Module.Category.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        setNoWeather(true);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        setNoWeather(false);
    }

    public boolean getNoWeather() {
        return NoWeather;
    }

    public void setNoWeather(boolean NoWeather) {
        this.NoWeather = NoWeather;
    }
}