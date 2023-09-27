/*
 * Copyright (c) 2014-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;
// import dev.vili.haiku.setting.settings.NumberSetting;

public final class NoFall extends Module {
    public final BooleanSetting allowElytra = new BooleanSetting("Allow elytra",
            "Also tries to prevent fall damage while you are flying with an elytra.\\n" + //
                    "\\n" + //
                    "\"\n" + //
                    "\t\t\t+ \"\\u00a7c\\u00a7lWARNING:\\u00a7r This can sometimes cause you to\"\n" + //
                    "\t\t\t+ \" stop flying unexpectedly..-NoFallHack",
            false);

    public NoFall() {
        super("NoFall", "Doin't take fall damage.", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
        this.addSettings(allowElytra);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        ClientPlayerEntity player = mc.player;
        if (player == null)
            return;

        player.networkHandler.sendPacket(new OnGroundOnly(false));
        super.onDisable();
    }

    @HaikuSubscribe
    public void onUpdate(TickEvent event) {
        // do nothing in creative mode, since there is no fall damage anyway
        ClientPlayerEntity player = mc.player;
        if (player.isCreative())
            return;

        // pause when flying with elytra, unless allowed
        boolean fallFlying = player.isFallFlying();
        if (fallFlying && !allowElytra.isEnabled())
            return;

        // ignore small falls that can't cause damage,
        // unless CreativeFlight is enabled in survival mode
        if (!player.getAbilities().flying && player.fallDistance <= (fallFlying ? 1 : 2))
            return;

        // attempt to fix elytra weirdness, if allowed
        if (fallFlying && player.isSneaking()
                && !isFallingFastEnoughToCauseDamage(player))
            return;

        // send packet to stop fall damage
        player.networkHandler.sendPacket(new OnGroundOnly(true));
    }

    private boolean isFallingFastEnoughToCauseDamage(ClientPlayerEntity player) {
        return player.getVelocity().y < -0.5;
    }
}
