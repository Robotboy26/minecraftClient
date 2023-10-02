/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.combat;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.utils.InventoryUtils;

import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;
import net.minecraft.util.Hand;

public class AutoEat extends Module {
	public final BooleanSetting hunger = new BooleanSetting("Hunger-AutoEat", "Eats when you're bewlow a certain amount of hunger.", true);
	public final NumberSetting hungerAmount = new NumberSetting("HungerAmount-AutoEat", "The maximum hunger to eat at.", 14, 0.5, 20, 0.5);
	public final BooleanSetting health = new BooleanSetting("Health-AutoEat", "Eats when you're bewlow a certain amount of health.", false);
	public final NumberSetting healthAmount = new NumberSetting("HealthAmount-AutoEat", "The maximum health to eat at.", 14, 0.5, 20, 0.5);
	public final BooleanSetting gapples = new BooleanSetting("Gapples-AutoEat", "Eats golden apples.", true);
	public final BooleanSetting preferGapples = new BooleanSetting("PreferGapples-AutoEat", "Prefers golden apples avobe regular food.", false);
	public final BooleanSetting chorus = new BooleanSetting("Chorus-AutoEat", "Eats chorus fruit.", false);
	public final BooleanSetting poisonous = new BooleanSetting("Poisonous-AutoEat", "Eats poisonous food.", false);

	private boolean eating;

	public AutoEat() {
		super("AutoEat", "Automatically eats food for you.", GLFW.GLFW_KEY_UNKNOWN, Category.COMBAT);
		this.addSettings(hunger, hungerAmount, health, healthAmount, gapples, preferGapples, chorus, poisonous);
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		mc.options.useKey.setPressed(false);

		super.onDisable();
	}

	@HaikuSubscribe
	public void onTick(TickEvent event) {
		if (eating && mc.options.useKey.isPressed() && !mc.player.isUsingItem()) {
			eating = false;
			mc.options.useKey.setPressed(false);
		} else if (eating) {
			return;
		} else {
			mc.options.useKey.setPressed(false);
		}

		if (hunger.isEnabled() && mc.player.getHungerManager().getFoodLevel() <= hungerAmount.getValue()) {
			startEating();
		} else if (health.isEnabled() && (int) mc.player.getHealth() + (int) mc.player.getAbsorptionAmount() <= healthAmount.getValue()) {
			startEating();
		}
	}

	private void startEating() {
		boolean gapplesB = gapples.isEnabled();
		boolean preferGapplesB = preferGapples.isEnabled();
		boolean chorusB = chorus.isEnabled();
		boolean poisonB = poisonous.isEnabled();

		int slot = -1;
		int hunger = -1;
		for (int s: InventoryUtils.getInventorySlots(true)) {
			FoodComponent food = mc.player.getInventory().getStack(s).getItem().getFoodComponent();

			if (food == null)
				continue;

			int h = preferGapplesB && (food == FoodComponents.GOLDEN_APPLE || food == FoodComponents.ENCHANTED_GOLDEN_APPLE)
					? Integer.MAX_VALUE : food.getHunger();

			if (h <= hunger
					|| (!gapplesB && (food == FoodComponents.GOLDEN_APPLE || food == FoodComponents.ENCHANTED_GOLDEN_APPLE))
					|| (!chorusB && food == FoodComponents.CHORUS_FRUIT)
					|| (!poisonB && isPoisonous(food)))
				continue;

			slot = s;
			hunger = h;
		}

		if (hunger != -1) {
			if (slot == mc.player.getInventory().selectedSlot || slot == 40) {
				mc.options.useKey.setPressed(true);
				mc.interactionManager.interactItem(mc.player, slot == 40 ? Hand.OFF_HAND : Hand.MAIN_HAND);
				eating = true;
			} else {
				InventoryUtils.selectSlot(slot);
			}
		}
	}

	private boolean isPoisonous(FoodComponent food) {
		return food.getStatusEffects().stream().anyMatch(e -> e.getFirst().getEffectType().getCategory() == StatusEffectCategory.HARMFUL);
	}
}
