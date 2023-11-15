package dev.vili.haiku.module.modules.client;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.module.Module;

public final class Notifications extends Module {
    private static Notifications instance;

    public Notifications() {
        super("Notifications", "Notifications", GLFW.GLFW_KEY_UNKNOWN, Category.CLIENT);
        instance = this;
    }

    public static Notifications getInstance() {
        return instance;
    }
}
