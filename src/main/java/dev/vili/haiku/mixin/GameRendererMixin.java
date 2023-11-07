/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.vili.haiku.Haiku;
import dev.vili.haiku.event.events.render.Render3DEvent;
import dev.vili.haiku.event.events.render.RenderAfterWorldEvent;
import dev.vili.haiku.mixinterface.IVec3d;
import dev.vili.haiku.module.modules.player.Reach;
import dev.vili.haiku.module.modules.render.Freecam;
import dev.vili.haiku.render.Renderer3D;
import dev.vili.haiku.utils.Utils;
import dev.vili.haiku.utils.render.NametagUtils;
import dev.vili.haiku.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.HitResult;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final MinecraftClient client;

    @Shadow public abstract void updateTargetedEntity(float tickDelta);

    @Shadow public abstract void reset();

    @Shadow @Final private Camera camera;
    @Unique private Renderer3D renderer;

    @Inject(method = "renderWorld", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = { "ldc=hand" }), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onRenderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo info, boolean bl, Camera camera, MatrixStack matrixStack, double d, float f, float g, Matrix4f matrix4f, Matrix3f matrix3f) {
        if (!Utils.canUpdate()) return;

        client.getProfiler().push(Haiku.MOD_NAME + "_render");

        if (renderer == null) renderer = new Renderer3D();
        Render3DEvent event = Render3DEvent.get(matrices, renderer, tickDelta, camera.getPos().x, camera.getPos().y, camera.getPos().z);

        RenderUtils.updateScreenCenter();
        NametagUtils.onRender(matrices, matrix4f);

        renderer.begin();
        Haiku.getInstance().getEventBus().post(event);
        renderer.render(matrices);

        RenderSystem.applyModelViewMatrix();
        client.getProfiler().pop();
    }

    @Inject(method = "renderWorld", at = @At("TAIL"))
    private void onRenderWorldTail(CallbackInfo info) {
        Haiku.getInstance().getEventBus().post(RenderAfterWorldEvent.get());
    }

    // @Inject(method = "updateTargetedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"), cancellable = true)
    // private void onUpdateTargetedEntity(float tickDelta, CallbackInfo info) {
    //     if (Modules.get().get(NoMiningTrace.class).canWork() && client.crosshairTarget.getType() == HitResult.Type.BLOCK) {
    //         client.getProfiler().pop();
    //         info.cancel();
    //     }
    // }

    // @Redirect(method = "updateTargetedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;raycast(DFZ)Lnet/minecraft/util/hit/HitResult;"))
    // private HitResult updateTargetedEntityEntityRayTraceProxy(Entity entity, double maxDistance, float tickDelta, boolean includeFluids) {
    //     if (Modules.get().isActive(LiquidInteract.class)) {
    //         HitResult result = entity.raycast(maxDistance, tickDelta, includeFluids);
    //         if (result.getType() != HitResult.Type.MISS) return result;

    //         return entity.raycast(maxDistance, tickDelta, true);
    //     }
    //     return entity.raycast(maxDistance, tickDelta, includeFluids);
    // }

    @Inject(method = "showFloatingItem", at = @At("HEAD"), cancellable = true)
    private void onShowFloatingItem(ItemStack floatingItem, CallbackInfo info) {
        if (floatingItem.getItem() == Items.TOTEM_OF_UNDYING && Haiku.getInstance().getModuleManager().isModuleEnabled("NoRender")) {
            info.cancel();
        }
    }

    @ModifyExpressionValue(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"))
    private float applyCameraTransformationsMathHelperLerpProxy(float original) {
        return Haiku.getInstance().getModuleManager().isModuleEnabled("NoRender") ? 0 : original;
    }

    // Freecam
    // @Inject(method = "updateTargetedEntity", at = @At("HEAD"), cancellable = true)
    // private void updateTargetedEntityInvoke(float tickDelta, CallbackInfo info) {
    //     Freecam freecam = (Freecam) Haiku.getInstance().getModuleManager().get(Freecam.class);
    //     boolean highwayBuilder = Haiku.getInstance().getModuleManager().isModuleEnabled("HighwayBuilder");

    //     if ((freecam.enabled || highwayBuilder) && client.getCameraEntity() != null && !freecamSet) {
    //         info.cancel();
    //         Entity cameraE = client.getCameraEntity();

    //         double x = cameraE.getX();
    //         double y = cameraE.getY();
    //         double z = cameraE.getZ();
    //         double prevX = cameraE.prevX;
    //         double prevY = cameraE.prevY;
    //         double prevZ = cameraE.prevZ;
    //         float yaw = cameraE.getYaw();
    //         float pitch = cameraE.getPitch();
    //         float prevYaw = cameraE.prevYaw;
    //         float prevPitch = cameraE.prevPitch;

    //         // if (highwayBuilder) {
    //         //     cameraE.setYaw(camera.getYaw());
    //         //     cameraE.setPitch(camera.getPitch());
    //         // }
    //         // else {
    //         //     ((IVec3d) cameraE.getPos()).set(freecam.pos.x, freecam.pos.y - cameraE.getEyeHeight(cameraE.getPose()), freecam.pos.z);
    //         //     cameraE.prevX = freecam.prevPos.x;
    //         //     cameraE.prevY = freecam.prevPos.y - cameraE.getEyeHeight(cameraE.getPose());
    //         //     cameraE.prevZ = freecam.prevPos.z;
    //         //     cameraE.setYaw(freecam.yaw);
    //         //     cameraE.setPitch(freecam.pitch);
    //         //     cameraE.prevYaw = freecam.prevYaw;
    //         //     cameraE.prevPitch = freecam.prevPitch;
    //         // }

    //         freecamSet = true;
    //         updateTargetedEntity(tickDelta);
    //         freecamSet = false;

    //         ((IVec3d) cameraE.getPos()).set(x, y, z);
    //         cameraE.prevX = prevX;
    //         cameraE.prevY = prevY;
    //         cameraE.prevZ = prevZ;
    //         cameraE.setYaw(yaw);
    //         cameraE.setPitch(pitch);
    //         cameraE.prevYaw = prevYaw;
    //         cameraE.prevPitch = prevPitch;
    //    }
    // }

    // @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    // private void renderHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo info) {
    //     if (!Modules.get().get(Freecam.class).renderHands() ||
    //         !Modules.get().get(Zoom.class).renderHands())
    //         info.cancel();
    // }
    //
    // @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 3))
    // private double updateTargetedEntityModifySurvivalReach(double d) {
    //     return Modules.get().get(Reach.class).entityReach();
    // }

    // @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 9))
    // private double updateTargetedEntityModifySquaredMaxReach(double d) {
    //     Reach reach = Modules.get().get(Reach.class);
    //     return reach.entityReach() * reach.entityReach();
    // }
}