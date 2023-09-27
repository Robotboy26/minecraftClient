package dev.vili.haiku.module.modules.player;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;

import org.lwjgl.glfw.GLFW;

public class Invisability extends Module 
{

    public Invisability() {
        super("Invisability", "Trys to make you invisable", GLFW.GLFW_KEY_UNKNOWN, Category.PLAYER);
    }

    @Override
    public void onEnable() {
        mc.player.setInvisible(true);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.player.setInvisible(false);
        super.onDisable();
    }

    @HaikuSubscribe
    private void onTick(TickEvent event) {
        mc.player.setInvisible(true);
    }
}