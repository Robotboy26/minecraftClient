package dev.vili.haiku.module.modules.movement;

import dev.vili.haiku.module.Module;
import org.lwjgl.glfw.GLFW;

public final class FallFly extends Module 
{
    public FallFly() {
        super("FallFly", "Pretend you have elytra on.",  GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
    }
    @Override
    public void onEnable() {
        mc.player.startFallFlying();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.player.stopFallFlying();
        super.onDisable();
    }
}