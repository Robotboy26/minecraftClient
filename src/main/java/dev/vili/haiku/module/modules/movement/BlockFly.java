/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.module.modules.movement;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.setting.settings.ModeSetting;
import dev.vili.haiku.module.Module;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;

public class BlockFly extends Module {
    public final ModeSetting mode = new ModeSetting("Mode", "What mode.", "Ghost", "Ghost");
    public BlockFly() {
        super("BlockFly", "BlockFly", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
    }

    BlockState state = null;
    BlockPos pos = null;

    @HaikuSubscribe
    public void onTick(TickEvent event) {
        assert mc.world != null;
        assert mc.player != null;
        if (mc.player.getBlockPos().add(0, -1, 0) != pos && pos != null && state != null) {
            mc.world.setBlockState(pos, state);
        }

        pos = mc.player.getBlockPos().add(0, -1, 0);
        state = mc.world.getBlockState(pos);

        if (!mc.options.sneakKey.isPressed() && mc.world.getBlockState(pos).getBlock() instanceof AirBlock && pos != null) {
            mc.world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
        }

        if (mc.options.sneakKey.isPressed() && mc.world.getBlockState(pos).getBlock() instanceof AirBlock && pos != null) {
            mc.world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    @Override
    public void onDisable() {
        assert mc.world != null;
        mc.world.setBlockState(pos, state);
        pos = null;
        BlockState state = null;
    }
}