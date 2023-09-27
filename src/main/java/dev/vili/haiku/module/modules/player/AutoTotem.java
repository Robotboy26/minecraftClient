/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.player;

import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.event.events.TickEvent;

import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import org.lwjgl.glfw.GLFW;

public class AutoTotem extends Module {

	public final BooleanSetting override = new BooleanSetting("Override-AutoTotem", "Equips a totem even if theres another item in the offhand.", false);
	public final NumberSetting delay = new NumberSetting("Delay-AutoTotem", "Minimum delay between equipping totems (in ticks).", 0, 0, 10, 1);
	public final NumberSetting popDelay = new NumberSetting("PopDelay-AutoTotem", "How long to wait after popping to equip a new totem (in ticks).", 0, 0, 10, 1);

	private boolean holdingTotem;
	private double delayint;

	public AutoTotem() {
		super("AutoTotem", "Automatically equips totems.", GLFW.GLFW_KEY_UNKNOWN, Category.PLAYER);
	}

	@HaikuSubscribe
	public void onTick(TickEvent event) {
		if (holdingTotem && mc.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
			delayint = delay.getValue();
		}

		holdingTotem = mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING;

		if (delayint > 0) {
			delayint--;
			return;
		}

		if (holdingTotem || (!mc.player.getOffHandStack().isEmpty() && !override.isEnabled())) {
			return;
		}

		// Cancel at all non-survival-inventory containers
		if (mc.player.playerScreenHandler == mc.player.currentScreenHandler) {
			for (int i = 9; i < 45; i++) {
				if (mc.player.getInventory().getStack(i >= 36 ? i - 36 : i).getItem() == Items.TOTEM_OF_UNDYING) {
					boolean itemInOffhand = !mc.player.getOffHandStack().isEmpty();
					mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
					mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player);

					if (itemInOffhand)
						mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);

					delayint = delay.getValue();
					return;
				}
			}
		} else {
			// If the player is in another inventory, atleast check the hotbar for anything to swap
			for (int i = 0; i < 9; i++) {
				if (mc.player.getInventory().getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
					if (i != mc.player.getInventory().selectedSlot) {
						mc.player.getInventory().selectedSlot = i;
						mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(i));
					}

					mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
					delayint = delay.getValue();
					return;
				}
			}
		}
	}

}
