package dev.vili.haiku.module.modules.render;

import org.lwjgl.glfw.GLFW;
import dev.vili.haiku.module.Module;

public class NoHurtCam extends Module {

    public NoHurtCam() {
        super("NoHurtCam", "camera will not have the hurt effect", GLFW.GLFW_KEY_UNKNOWN, Module.Category.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
