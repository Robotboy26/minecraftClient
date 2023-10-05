/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.utils.BleachQueue;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

public class AutoArmor extends Module {
	public final BooleanSetting antiBreak = new BooleanSetting("AntiBreak-AutoArmor", "Unequips your armor when its about to break.", false);
	public final BooleanSetting preferElytra = new BooleanSetting("PreferElytra-AutoArmor", "Equips elytras instead of chestplates when possible.", false);
	public final BooleanSetting elytraSwap = new BooleanSetting("SwapElytra", "This will swap to your elytra (if you have one) whenever you just so you can make a quick getaway", false);
	public final NumberSetting elytraSwapDelay = new NumberSetting("ElytraSwapDelay", "how long to wait before swapping to elytra (this only applys when elytra swap is enabled and this only applys to when you are jumping)", 0, 0, 20, 1);
	public final BooleanSetting delay = new BooleanSetting("Delay-AutoArmor", "Adds a delay between equipping armor pieces.", false);
	public final NumberSetting delayAmount = new NumberSetting("DelayAmount-AutoArmor", "How many ticks between putting on armor pieces.", 0, 20, 1, 0);

	private int tickDelay = 0;
	private int elytraDelay = 0;

	public AutoArmor() {
		super("AutoArmor", "Automatically equips armor.", GLFW.GLFW_KEY_UNKNOWN, Category.COMBAT);
		this.addSettings(antiBreak, preferElytra, elytraSwap, elytraSwapDelay, delay, delayAmount);
	}

	@HaikuSubscribe
	public void onTick(TickEvent event) {
		boolean shouldWearElytra = preferElytra.isEnabled();
		if (mc.player.playerScreenHandler != mc.player.currentScreenHandler || !BleachQueue.isEmpty("autoarmor_equip"))
			return;

		if (mc.player.isOnGround()) {
			elytraDelay = (int) elytraSwapDelay.getValue();
			shouldWearElytra = false;
		}

		if (elytraSwap.isEnabled() && elytraDelay > 0) {
			elytraDelay--;
			return;
		}

		if (elytraSwap.isEnabled() && elytraDelay ==0) {
			if (!mc.player.isOnGround()) {
				shouldWearElytra = true;
			}
		}

		if (tickDelay > 0) {
			tickDelay--;
			return;
		}

		tickDelay = (delay.isEnabled() ? (int) delayAmount.getValue() : 0);

		/* [Slot type, [Armor slot, Armor prot, New armor slot, New armor prot]] */
		Map<EquipmentSlot, int[]> armorMap = new HashMap<>(4);
		armorMap.put(EquipmentSlot.FEET, new int[] { 36, getProtection(mc.player.getInventory().getStack(36), shouldWearElytra), -1, -1 });
		armorMap.put(EquipmentSlot.LEGS, new int[] { 37, getProtection(mc.player.getInventory().getStack(37), shouldWearElytra), -1, -1 });
		armorMap.put(EquipmentSlot.CHEST, new int[] { 38, getProtection(mc.player.getInventory().getStack(38), shouldWearElytra), -1, -1 });
		armorMap.put(EquipmentSlot.HEAD, new int[] { 39, getProtection(mc.player.getInventory().getStack(39), shouldWearElytra), -1, -1 });

		/* Anti Break */
		if (antiBreak.isEnabled()) {
			for (Entry<EquipmentSlot, int[]> e: armorMap.entrySet()) {
				ItemStack is = mc.player.getInventory().getStack(e.getValue()[0]);
				int armorSlot = (e.getValue()[0] - 34) + (39 - e.getValue()[0]) * 2;

				if (is.isDamageable() && is.getMaxDamage() - is.getDamage() < 7) {
					/* Look for an empty slot to quick move to */
					int forceMoveSlot = -1;
					for (int s = 0; s < 36; s++) {
						if (mc.player.getInventory().getStack(s).isEmpty()) {
							mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, armorSlot, 1, SlotActionType.QUICK_MOVE, mc.player);
							return;
						} else if (!(mc.player.getInventory().getStack(s).getItem() instanceof ToolItem)
								&& !(mc.player.getInventory().getStack(s).getItem() instanceof ArmorItem)
								&& !(mc.player.getInventory().getStack(s).getItem() instanceof ElytraItem)
								&& mc.player.getInventory().getStack(s).getItem() != Items.TOTEM_OF_UNDYING && forceMoveSlot == -1) {
							forceMoveSlot = s;
						}
					}

					/* Bruh no empty spots, then force move to a non-totem/tool/armor item */
					if (forceMoveSlot != -1) {
						//System.out.println(forceMoveSlot);
						mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId,
								forceMoveSlot < 9 ? 36 + forceMoveSlot : forceMoveSlot, 1, SlotActionType.THROW, mc.player);
						mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, armorSlot, 1, SlotActionType.QUICK_MOVE, mc.player);
						return;
					}

					/* No spots to move to, yeet the armor to not cause any bruh moments */
					mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, armorSlot, 1, SlotActionType.THROW, mc.player);
					return;
				}
			}
		}

		for (int s = 0; s < 36; s++) {
			int prot = getProtection(mc.player.getInventory().getStack(s), shouldWearElytra);

			if (prot > 0) {
				EquipmentSlot slot = (mc.player.getInventory().getStack(s).getItem() instanceof ElytraItem
						? EquipmentSlot.CHEST : ((ArmorItem) mc.player.getInventory().getStack(s).getItem()).getSlotType());

				for (Entry<EquipmentSlot, int[]> e: armorMap.entrySet()) {
					if (e.getKey() == slot) {
						if (prot > e.getValue()[1] && prot > e.getValue()[3]) {
							e.getValue()[2] = s;
							e.getValue()[3] = prot;
						}
					}
				}
			}
		}

		for (Entry<EquipmentSlot, int[]> e: armorMap.entrySet()) {
			if (e.getValue()[2] != -1) {
				if (e.getValue()[1] == -1 && e.getValue()[2] < 9) {
					if (e.getValue()[2] != mc.player.getInventory().selectedSlot) {
						mc.player.getInventory().selectedSlot = e.getValue()[2];
						mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(e.getValue()[2]));
					}

					mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 36 + e.getValue()[2], 1, SlotActionType.QUICK_MOVE, mc.player);
				} else if (mc.player.playerScreenHandler == mc.player.currentScreenHandler) {
					/* Convert inventory slots to container slots */
					int armorSlot = (e.getValue()[0] - 34) + (39 - e.getValue()[0]) * 2;
					int newArmorslot = e.getValue()[2] < 9 ? 36 + e.getValue()[2] : e.getValue()[2];

					mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, newArmorslot, 0, SlotActionType.PICKUP, mc.player);
					mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, armorSlot, 0, SlotActionType.PICKUP, mc.player);

					if (e.getValue()[1] != -1)
						mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, newArmorslot, 0, SlotActionType.PICKUP, mc.player);
				}

				return;
			}
		}
	}

	private int getProtection(ItemStack is, boolean shouldWearElytra) {
		if (is.getItem() instanceof ArmorItem || is.getItem() == Items.ELYTRA) {
			int prot = 0;

			if (is.getItem() instanceof ElytraItem) {
				if (!ElytraItem.isUsable(is))
					return 0;

				if (shouldWearElytra) {
					prot = 32767;
				} else {
					prot = 1;
				}
			} else if (is.getMaxDamage() - is.getDamage() < 7 && antiBreak.isEnabled()) {
				return 0;
			}

			if (is.hasEnchantments()) {
				for (Entry<Enchantment, Integer> e: EnchantmentHelper.get(is).entrySet()) {
					if (e.getKey() instanceof ProtectionEnchantment)
						prot += e.getValue();
				}
			}

			return (is.getItem() instanceof ArmorItem ? ((ArmorItem) is.getItem()).getProtection() : 0) + prot;
		} else if (!is.isEmpty()) {
			return 0;
		}

		return -1;
	}
}
