package dev.vili.haiku.module.modules.client;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.module.Module;

public final class OptifineCapes extends Module {
    private static OptifineCapes instance;

    public OptifineCapes() {
        super("OptifineCapes", "OptifineCapes", GLFW.GLFW_KEY_UNKNOWN, Category.CLIENT);
        instance = this;
    }

    public static OptifineCapes getInstance() {
        return instance;
    }
}
