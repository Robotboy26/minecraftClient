/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.utils;

import dev.vili.haiku.mixinterface.IMinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static dev.vili.haiku.Haiku.mc;

import org.joml.Vector3d;

public class Utils {
    public static double frameTime;
    public static void rightClick() {
        ((IMinecraftClient) mc).rightClick();
    }

    public static Vector3d set(Vector3d vec, Vec3d v) {
        vec.x = v.x;
        vec.y = v.y;
        vec.z = v.z;

        return vec;
    }

    public static boolean canUpdate() {
        return mc != null && mc.world != null && mc.player != null;
    }

    public static Vector3d set(Vector3d vec, Entity entity, double tickDelta) {
        vec.x = MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
        vec.y = MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
        vec.z = MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());

        return vec;
    }
}
