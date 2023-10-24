/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.module.modules.movement;

import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.setting.settings.NumberSetting;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.mixinterface.IVec3d;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.mixin.AbstractBlockAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class Anchor extends Module {
    private final NumberSetting MaxHeight = new NumberSetting("MaxHeight-Anchor", "The maximum height Anchor will work at.", 20, 0,  255, 0.01);
    private final NumberSetting MinPitch = new NumberSetting("MinPitch-Anchor", "The minimum pitch at which anchor will work.", 0, -90, 90, 0.01);
    private final BooleanSetting CancelJumpInHole = new BooleanSetting("CancelJumpInHole-Anchor", "Prevents you from jumping when Anchor is active and Min Pitch is met.", false);
    private final BooleanSetting Pull = new BooleanSetting("Pull-Anchor", "The pull strength of Anchor.", false);
    private final NumberSetting PullSpeed = new NumberSetting("PullSpeed-Anchor", "How fast to pull towards the hole in blocks per second.", 5, 0, 5, 0.01);

    private final BlockPos.Mutable blockPos = new BlockPos.Mutable();
    private boolean wasInHole;
    private boolean foundHole;
    private int holeX, holeZ;

    public boolean cancelJump;

    public boolean controlMovement;
    public double deltaX, deltaZ;

    public Anchor() {
        super("Anchor", "Helps you get into holes by stopping your movement completely over a hole.", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
        this.addSettings(MaxHeight, MinPitch, CancelJumpInHole, Pull, PullSpeed);
    }

    @Override
    public void onEnable() {
        wasInHole = false;
        holeX = holeZ = 0;
    }

    @HaikuSubscribe
    private void onTick(TickEvent event) {
        cancelJump = foundHole && CancelJumpInHole.isEnabled() && mc.player.getPitch() >= MinPitch.getValue();
        controlMovement = false;

        int x = MathHelper.floor(mc.player.getX());
        int y = MathHelper.floor(mc.player.getY());
        int z = MathHelper.floor(mc.player.getZ());

        if (isHole(x, y, z)) {
            wasInHole = true;
            holeX = x;
            holeZ = z;
            return;
        }

        if (wasInHole && holeX == x && holeZ == z) return;
        else if (wasInHole) wasInHole = false;

        if (mc.player.getPitch() < MinPitch.getValue()) return;

        foundHole = false;
        double holeX = 0;
        double holeZ = 0;

        for (int i = 0; i < MaxHeight.getValue(); i++) {
            y--;
            if (y <= mc.world.getBottomY() || !isAir(x, y, z)) break;

            if (isHole(x, y, z)) {
                foundHole = true;
                holeX = x + 0.5;
                holeZ = z + 0.5;
                break;
            }
        }

        if (foundHole) {
            controlMovement = true;
            deltaX = MathHelper.clamp(holeX - mc.player.getX(), -0.05, 0.05);
            deltaZ = MathHelper.clamp(holeZ - mc.player.getZ(), -0.05, 0.05);

            ((IVec3d) mc.player.getVelocity()).set(deltaX, mc.player.getVelocity().y - (Pull.isEnabled() ? PullSpeed.getValue() : 0), deltaZ);
        }
    }

    private boolean isHole(int x, int y, int z) {
        return isHoleBlock(x, y - 1, z) &&
                isHoleBlock(x + 1, y, z) &&
                isHoleBlock(x - 1, y, z) &&
                isHoleBlock(x, y, z + 1) &&
                isHoleBlock(x, y, z - 1);
    }

    private boolean isHoleBlock(int x, int y, int z) {
        blockPos.set(x, y, z);
        Block block = mc.world.getBlockState(blockPos).getBlock();
        return block == Blocks.BEDROCK || block == Blocks.OBSIDIAN || block == Blocks.CRYING_OBSIDIAN;
    }

    private boolean isAir(int x, int y, int z) {
        blockPos.set(x, y, z);
        return !((AbstractBlockAccessor)mc.world.getBlockState(blockPos).getBlock()).isCollidable();
    }
}
