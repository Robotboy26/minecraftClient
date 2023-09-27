/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.movement;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.NumberSetting;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;

import org.lwjgl.glfw.GLFW;

public class Fly extends Module 
{
    public final NumberSetting speedFlyHack = new NumberSetting("Speed-FlyHack", "How fast to fly.", 1.5, 0, 10, 0.01);
    private int antiKickTimer = 0;

    public Fly() {
        super("Fly", "Allows you to fly.", GLFW.GLFW_KEY_F, Category.MOVEMENT);
        this.addSettings(speedFlyHack);
    }

    @Override
    public void onDisable() {
        if (mc.world == null || mc.player == null) return;
        mc.player.getAbilities().flying = false;
        mc.player.getAbilities().allowFlying = false;
        mc.player.getAbilities().setFlySpeed(0.05f);

        super.onDisable();
    }

    @HaikuSubscribe
    public void onTick(TickEvent event) {
        Double flySpeed = speedFlyHack.getValue();
        if (flySpeed != null) {
            if (mc.world == null || mc.player == null) return;
            mc.player.getAbilities().flying = true;
            mc.player.getAbilities().allowFlying = true;
            mc.player.getAbilities().setFlySpeed((float) (flySpeed / 40.0));

            antiKickTimer++;
            if (antiKickTimer > 20 && mc.player.getWorld().getBlockState(BlockPos.ofFloored(mc.player.getPos().subtract(0, 0.0433D, 0))).isAir()) {
                antiKickTimer = 0;
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.0433D, mc.player.getZ(), false));
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() - 0.0433D, mc.player.getZ(), true));
            }
        }
    }
}
