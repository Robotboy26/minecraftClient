// /*
//  * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
//  * Copyright (c) Meteor Development.
//  */

// package dev.vili.haiku.module.modules.player;

// import org.lwjgl.glfw.GLFW;

// import dev.vili.haiku.event.game.OpenScreenEvent;
// import dev.vili.haiku.eventbus.HaikuSubscribe;
// import dev.vili.haiku.module.Module;
// import net.minecraft.client.gui.screen.DeathScreen;

// public class AutoRespawn extends Module {
//     public AutoRespawn() {
//         super("AutoRespawn", "Automatically respawns after death.", GLFW.GLFW_KEY_UNKNOWN, Category.PLAYER);
//     }

//     @Override
//     public void onEnable() {
//         if (mc.player == null) return;
//         if (mc.player.isAlive()) return;

//         mc.player.requestRespawn();
//         super.onEnable();
//     }

//     @Override
//     public void onDisable() {
//         super.onDisable();
//     }

//     @EventHandler
//     private void onOpenScreenEvent(OpenScreenEvent event) {
//         this.onDisable();
//         if (!(event.screen instanceof DeathScreen)) return;

//         mc.player.requestRespawn();
//         event.setCancelled(true);
//     }
// }
