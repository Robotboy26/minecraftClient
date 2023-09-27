/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.module.modules.movement;

import dev.vili.haiku.event.events.packets.PacketEvent;
import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.setting.settings.NumberSetting;
import net.minecraft.block.BlockState;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

import org.lwjgl.glfw.GLFW;

public class ClickTP extends Module {
    public final NumberSetting maxDistance = new NumberSetting("max-distance", "The maximum distance you can teleport.", 5, 0, 100, 0.1);

    public ClickTP() {
        super("ClickTP", "Teleports you to the block you click on.", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @HaikuSubscribe
    private void onTick(TickEvent event) {
        if (mc.player.isUsingItem()) return;

        HitResult hitResult = mc.player.raycast(maxDistance.getValue(), 1f / 20f, false);

        if (hitResult.getType() == HitResult.Type.ENTITY && mc.player.interact(((EntityHitResult) hitResult).getEntity(), Hand.MAIN_HAND) != ActionResult.PASS) return;

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = ((BlockHitResult) hitResult).getBlockPos();
            Direction side = ((BlockHitResult) hitResult).getSide();

            if (mc.world.getBlockState(pos).onUse(mc.world, mc.player, Hand.MAIN_HAND, (BlockHitResult) hitResult) != ActionResult.PASS) return;

            BlockState state = mc.world.getBlockState(pos);

            VoxelShape shape = state.getCollisionShape(mc.world, pos);
            if (shape.isEmpty()) shape = state.getOutlineShape(mc.world, pos);

            double height = shape.isEmpty() ? 1 : shape.getMax(Direction.Axis.Y);

            mc.player.setPosition(pos.getX() + 0.5 + side.getOffsetX(), pos.getY() + height, pos.getZ() + 0.5 + side.getOffsetZ());
            this.onDisable();
        }
    }
}
