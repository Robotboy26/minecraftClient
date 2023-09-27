package dev.vili.haiku.module.modules.player;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;

import org.lwjgl.glfw.GLFW;

public class Invulnerable extends Module 
{
    public Invulnerable() {
        super("Invulnerable", "Trys to make you Invulnerable", GLFW.GLFW_KEY_UNKNOWN, Category.PLAYER);
    }

    @Override
    public void onEnable() {
        mc.player.setInvulnerable(true);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.player.setInvulnerable(false);
        super.onDisable();
    }

    @HaikuSubscribe
    private void onTick(TickEvent event) {
        mc.player.setInvulnerable(true);
    }
}
