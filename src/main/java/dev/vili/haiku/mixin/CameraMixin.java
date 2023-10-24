/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.mixin;

import dev.vili.haiku.Haiku;
import dev.vili.haiku.mixinterface.ICamera;
import dev.vili.haiku.module.modules.render.Freecam;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public abstract class CameraMixin implements ICamera {
    @Shadow private boolean thirdPerson;

    @Shadow private float yaw;
    @Shadow private float pitch;

    @Shadow protected abstract void setRotation(float yaw, float pitch);

    @Unique
    private float tickDelta;

    // @Inject(method = "getSubmersionType", at = @At("HEAD"), cancellable = true)
    // private void getSubmergedFluidState(CallbackInfoReturnable<CameraSubmersionType> ci) {
    //     if (Haiku.getInstance().getModuleManager().isModuleSettingEnabled("NoRender", "")) ci.setReturnValue(CameraSubmersionType.NONE);
    // }

    @ModifyVariable(method = "clipToSpace", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private double modifyClipToSpace(double d) {
        return (Haiku.getInstance().getModuleManager().isModuleEnabled("Freecam") ? 0: d);
    }

    // @Inject(method = "clipToSpace", at = @At("HEAD"), cancellable = true)
    // private void onClipToSpace(double desiredCameraDistance, CallbackInfoReturnable<Double> info) {
    //     if (Modules.get().get(CameraTweaks.class).clip()) {
    //         info.setReturnValue(desiredCameraDistance);
    //     }
    // }

    @Inject(method = "update", at = @At("HEAD"))
    private void onUpdateHead(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo info) {
        this.tickDelta = tickDelta;
    }

    @Inject(method = "update", at = @At("TAIL"))
    private void onUpdateTail(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo info) {
        if (Haiku.getInstance().getModuleManager().isModuleEnabled("Freecam")) {
            this.thirdPerson = true;
        }
    }
    
    // @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    // private void onUpdateSetPosArgs(Args args) {
    //     Freecam freecam = (Freecam) Haiku.getInstance().getModuleManager().getModule("Freecam");

    //     if (freecam.isEnabled()) {
    //         args.set(0, freecam.getX(tickDelta));
    //         args.set(1, freecam.getY(tickDelta));
    //         args.set(2, freecam.getZ(tickDelta));
    //     }
    // }

    // @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    // private void onUpdateSetRotationArgs(Args args) {
    //     Freecam freecam = Modules.get().get(Freecam.class);
    //     FreeLook freeLook = Modules.get().get(FreeLook.class);

    //     if (freecam.isActive()) {
    //         args.set(0, (float) freecam.getYaw(tickDelta));
    //         args.set(1, (float) freecam.getPitch(tickDelta));
    //     }
    //     else if (Modules.get().isActive(HighwayBuilder.class)) {
    //         args.set(0, yaw);
    //         args.set(1, pitch);
    //     }
    //     else if (freeLook.isActive()) {
    //         args.set(0, freeLook.cameraYaw);
    //         args.set(1, freeLook.cameraPitch);
    //     }
    // }

    @Override
    public void setRot(double yaw, double pitch) {
        setRotation((float) yaw, (float) MathHelper.clamp(pitch, -90, 90));
    }
}
