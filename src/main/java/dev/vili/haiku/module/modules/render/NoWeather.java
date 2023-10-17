package dev.vili.haiku.module.modules.render;

import org.lwjgl.glfw.GLFW;
import dev.vili.haiku.event.events.render.Render3DEvent;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.utils.HaikuLogger;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventBus;
import java.util.Collections;

public class NoWeather extends Module {
    private final EventBus eventBus;

    public NoWeather() {
        super("Test", "Test", GLFW.GLFW_KEY_UNKNOWN, Module.Category.DEV);
        this.eventBus = new EventBus();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        mc.getInstance().worldRenderer.rende = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }