/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import dev.vili.haiku.event.events.entity.EntityAddedEvent;
import dev.vili.haiku.event.events.entity.EntityRemovedEvent;

import dev.vili.haiku.Haiku;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
    @Unique private final DimensionEffects endSky = new DimensionEffects.End();

    @Shadow @Nullable public abstract Entity getEntityById(int id);

    @Inject(method = "addEntityPrivate", at = @At("TAIL"))
    private void onAddEntityPrivate(int id, Entity entity, CallbackInfo info) {
        if (entity != null) Haiku.OEVENT_BUS.post(EntityAddedEvent.get(entity));
    }

    @Inject(method = "removeEntity", at = @At("HEAD"))
    private void onRemoveEntity(int entityId, Entity.RemovalReason removalReason, CallbackInfo info) {
        if (getEntityById(entityId) != null) Haiku.OEVENT_BUS.post(EntityRemovedEvent.get(getEntityById(entityId)));
    }

    /**
     * @author Walaryne
     */
    // @Inject(method = "getDimensionEffects", at = @At("HEAD"), cancellable = true)
    // private void onGetSkyProperties(CallbackInfoReturnable<DimensionEffects> info) {
    //     Boolean ambience = Haiku.getInstance().getModuleManager().isModuleEnabled("NoWeather");

    //     if (ambience) {
    //         info.setReturnValue(endSky);
    //     }
    // }

    /**
     * @author Walaryne
     */
    // @Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true)
    // private void onGetSkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Vec3d> info) {
    //     Boolean ambience = Haiku.getInstance().getModuleManager().isModuleEnabled("NoWeather");

    //     if (ambience) {
    //         info.setReturnValue(ambience.skyColor().getVec3d());
    //     }
    // }

    /**
     * @author Walaryne
     */
    // @Inject(method = "getCloudsColor", at = @At("HEAD"), cancellable = true)
    // private void onGetCloudsColor(float tickDelta, CallbackInfoReturnable<Vec3d> info) {
    //     Boolean ambience = Haiku.getInstance().getModuleManager().isModuleEnabled("NoWeather");

    //     if (ambience.isActive() && ambience.customCloudColor.get()) {
    //         info.setReturnValue(ambience.cloudColor.get().getVec3d());
    //     }
    // }

    @ModifyArgs(method = "doRandomBlockDisplayTicks", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;randomBlockDisplayTick(IIIILnet/minecraft/util/math/random/Random;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos$Mutable;)V"))
    private void doRandomBlockDisplayTicks(Args args) {
        if (Haiku.getInstance().getModuleManager().isModuleEnabled("NoWeather")) {
            args.set(5, Blocks.BARRIER);
        }
    }
}
