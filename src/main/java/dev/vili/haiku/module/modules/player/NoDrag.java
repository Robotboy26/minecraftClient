package dev.vili.haiku.module.modules.player;

import dev.vili.haiku.module.Module;
import org.lwjgl.glfw.GLFW;

public final class NoDrag extends Module 
{
    public NoDrag() {
        super("NoDrag", "temp",  GLFW.GLFW_KEY_BACKSPACE, Category.PLAYER);
    }
    @Override
    public void onEnable() {
        mc.player.setNoDrag(true);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.player.setNoDrag(false);
        super.onDisable();
    }
}