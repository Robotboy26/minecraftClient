package dev.vili.haiku.module.modules.render;

import dev.vili.haiku.module.Module;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public final class FullBright extends Module 
{
    public FullBright() {
        super("FullBright", "See in the dark.", GLFW.GLFW_KEY_UNKNOWN, Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        MinecraftClient.getInstance().options.getGamma().setValue((double) 1000);
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        MinecraftClient.getInstance().options.getGamma().setValue((double) 1);
        super.onDisable();
    }
}

