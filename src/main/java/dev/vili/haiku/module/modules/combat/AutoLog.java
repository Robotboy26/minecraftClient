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
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.utils.HaikuLogger;
import dev.vili.haiku.utils.world.DamageUtils;
import dev.vili.haiku.utils.world.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

import net.minecraft.text.Text;

public class AutoLog extends Module {
	public final BooleanSetting health = new BooleanSetting("Health-AutoLog", "Disconnects when you're under a certain health.", true);
	public final NumberSetting healthValue = new NumberSetting("HealthValue-AutoLog", "The health to disconnects at.", 8, 1, 20, 0.5);
	public final BooleanSetting ignoreTotems = new BooleanSetting("IgnoreTotems-AutoLog", "Makes you disconnect even if you're carrying totems.", false);
	public final BooleanSetting vehicle = new BooleanSetting("Vehicle-AutoLog", "Also disconnects when your vehicle is below the specified health.", false);
	public final BooleanSetting oneHit = new BooleanSetting("OneHit-AutoLog", "Disconnects when a nearby player can kill you in one hit.", true);
	public final BooleanSetting crystal = new BooleanSetting("Crystal-AutoLog", "Disconnects when you're near an end crystal.", true);
	public final NumberSetting crystalDistance = new NumberSetting("CrystalDistance-AutoLog", "The maximum distance away from a crystal to disconnect.", 6, 1, 50, 1);
	public final BooleanSetting playerNearby = new BooleanSetting("PlayerNearby-AutoLog", "Disconnects when a player is in render distance/nearby", false);
	public final NumberSetting playerRange = new NumberSetting("PlayerRange-AutoLog", "The range to disconnect at.", 40, 1, 250, 1);
	public final BooleanSetting elytraLog = new BooleanSetting("ElytraLog-AutoLog", "Log you out if your elytra is low on duribility.", true);
	public final NumberSetting elytraDuribility = new NumberSetting("ElytraDuribility-AutoLog", "At what duribility to logout at.", 20, 1, 431, 1);
	public final BooleanSetting smartToggle = new BooleanSetting("SmartToggle-AutoLog", "Shows in the chat when AutoLog re-enables.", true);

	private boolean smartDisabled = false;

	public AutoLog() {
		super("AutoLog", "Automatically disconnects from servers.", GLFW.GLFW_KEY_UNKNOWN, Category.COMBAT);
		this.addSettings(health, healthValue, ignoreTotems, vehicle, oneHit, crystal, crystalDistance, playerNearby, playerRange, elytraLog, elytraDuribility, smartToggle);
	}

	@Override
	public void onDisable() {
		smartDisabled = false;
		super.onDisable();
	}

	@Override
	public void onEnable() {
		smartDisabled = false;
		super.onEnable();
	}

	@HaikuSubscribe
	public void onTick(TickEvent event) {
		Text logText = getLogText();
		
		if (smartDisabled && logText == null) {
			smartDisabled = false;
			
			if (smartToggle.isEnabled()) {
				HaikuLogger.info("Re-enabled AutoLog!");
			}
		} else if (!smartDisabled && logText != null) {
			log(logText);
		}
	}

	private Text getLogText() {
		boolean hasTotem = mc.player.getMainHandStack().getItem() == Items.TOTEM_OF_UNDYING 
				|| mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING;

		int playerHealth = (int) (mc.player.getHealth() + mc.player.getAbsorptionAmount());

		if (health.isEnabled()) {
			int healthInt = (int) healthValue.getValue();
			// HaikuLogger.info("Health: " + healthInt);

			if ((ignoreTotems.isEnabled() || !hasTotem) && playerHealth <= healthInt) {
				return Text.literal("[AutoLog] Your health (" + playerHealth + " HP) was lower than " + healthInt + " HP.");
			}

			if (vehicle.isEnabled() &&  mc.player.getVehicle() instanceof LivingEntity) {
				LivingEntity vehicleEntity = (LivingEntity) mc.player.getVehicle();
				int vehicleHealth = (int) (vehicleEntity.getHealth() + vehicleEntity.getAbsorptionAmount());

				if (vehicleHealth < healthInt) {
					return Text.literal("[AutoLog] Your vehicle health (" + vehicleHealth + " HP) was lower than " + health + " HP.");
				}
			}
		}

		if ((oneHit.isEnabled()) && !hasTotem) {
			for (PlayerEntity player: mc.world.getPlayers()) {
				if ((!oneHit.isEnabled()) || player == mc.player) {
					continue;
				}

				int attackDamage = (int) DamageUtils.getAttackDamage(player, mc.player);

				if (player.distanceTo(mc.player) <= 6 && attackDamage >= playerHealth) {
					return Text.literal("[AutoLog] " + player.getDisplayName().getString() + " could kill you (dealing " + attackDamage + " damage).");
				}
			}
		}

		if (crystal.isEnabled()) {
			for (Entity e: mc.world.getEntities()) {
				if (e instanceof EndCrystalEntity && mc.player.distanceTo(e) <= (float) crystalDistance.getValue()) {
					return Text.literal("[AutoLog] End crystal appeared within range.");
				}
			}
		}

		if (playerNearby.isEnabled()) {

			for (PlayerEntity player: mc.world.getPlayers()) {
				if (!EntityUtils.isOtherServerPlayer(player)
						|| (!playerNearby.isEnabled())) {
					continue;
				}

				if (playerNearby.isEnabled()) {
					for (PlayerEntity playerEntity: mc.world.getPlayers()) {
						if (!EntityUtils.isOtherServerPlayer(player) || !playerNearby.isEnabled()) {
							continue;
						}
						if (player.distanceTo(playerEntity) <= (float) playerRange.getValue()) {
							return Text.literal("[AutoLog] " + player.getDisplayName().getString() + " appeared " + (int) player.distanceTo(mc.player) + " blocks away.");
						}
					}
				}
					return Text.literal("[AutoLog] " + player.getDisplayName().getString() + " appeared " + (int) player.distanceTo(mc.player) + " blocks away.");
				}
		return null;
	}

	if (elytraLog.isEnabled()) {
		if (mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA) {
			int elytraDuribilityInt = (int) elytraDuribility.getValue();
			int elytraDuribility = mc.player.getEquippedStack(EquipmentSlot.CHEST).getDamage();

			if (elytraDuribility <= elytraDuribilityInt) {
				return Text.literal("[AutoLog] Your elytra is low on duribility (" + elytraDuribility + ").");
			}
		}
	}

		return null;
	}

	private void log(Text reason) {
		super.onDisable();
		mc.player.networkHandler.getConnection().disconnect(reason);

		if (smartToggle.isEnabled()) {
			smartDisabled = true;
		} else {
			setEnabled(false);
		}
	}

}
