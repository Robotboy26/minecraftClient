package dev.vili.haiku.module.modules.player;

import dev.vili.haiku.module.Module;
import org.lwjgl.glfw.GLFW;

public final class FastRegen extends Module 
{
    public FastRegen() {
        super("FastRegen", "You still need food though.",  GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
    }
    @Override
    public void onEnable() {
        mc.player.timeUntilRegen = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
