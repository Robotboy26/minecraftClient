package dev.nebula.mixin;
// /*
//  * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
//  * Copyright (c) Meteor Development.
//  */

// package dev.vili.haiku.mixin;

// import dev.vili.haiku.Haiku;
// import net.minecraft.client.render.LightmapTextureManager;
// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.ModifyArgs;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
// import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

// // add to this later  || Haiku.getInstance().getModuleManager().isModuleEnabled("Xray")

// @Mixin(LightmapTextureManager.class)
// public class LightmapTextureManagerMixin {
//     @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImage;setColor(III)V"))
//     private void update(Args args) {
//         if (Haiku.getInstance().getModuleManager().isModuleEnabled("FullBright")) {
//             args.set(2, 0xFFFFFFFF);
//         }
//     }

//     @Inject(method = "getDarknessFactor(F)F", at = @At("HEAD"), cancellable = true)
// 	private void getDarknessFactor(float tickDelta, CallbackInfoReturnable<Float> info) {
// 		if (Haiku.getInstance().getModuleManager().isModuleEnabled("AntiBlind")) info.setReturnValue(0.0f);
// 	}
// }
