/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.mixin;

import dev.vili.haiku.Haiku;
import dev.vili.haiku.MixinResources.mixinterface.ICamera;
import dev.vili.haiku.event.events.meteor.MouseButtonEvent;
import dev.vili.haiku.event.events.meteor.MouseScrollEvent;
import dev.vili.haiku.module.modules.render.Freecam;
import dev.vili.haiku.utils.misc.input.Input;
import dev.vili.haiku.utils.misc.input.KeyAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo info) {
        Input.setButtonState(button, action != GLFW_RELEASE);

        // if (Haiku.getInstance().getEventBus().post(MouseButtonEvent.get(button, KeyAction.get(action)))) info.cancel();
    }

    // @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    // private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo info) {
    //     if (MeteorClient.EVENT_BUS.post(MouseScrollEvent.get(vertical)).isCancelled()) info.cancel();
    // }

    // @Redirect(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"))
    // private void updateMouseChangeLookDirection(ClientPlayerEntity player, double cursorDeltaX, double cursorDeltaY) {
    //     Freecam freecam = (Freecam) Haiku.getInstance().getModuleManager().get(Freecam.class);

    //     if (freecam.isEnabled()) freecam.changeLookDirection(cursorDeltaX * 0.15, cursorDeltaY * 0.15);
    //     else player.changeLookDirection(cursorDeltaX, cursorDeltaY);
    // }
}
