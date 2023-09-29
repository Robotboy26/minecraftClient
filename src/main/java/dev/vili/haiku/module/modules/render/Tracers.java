/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.render;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.Haiku;
import dev.vili.haiku.event.events.EventWorldRender;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.ColorSetting;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.utils.render.Renderer;
import dev.vili.haiku.utils.render.color.LineColor;
import dev.vili.haiku.utils.world.EntityUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Vec3d;

public class Tracers extends Module {
	public final BooleanSetting players = new BooleanSetting("Players-Tracers", "Shows Tracers for Players.", true);
	public final ColorSetting playerColor = new ColorSetting("PlayerColor-Tracers", "Tracer color for players.", 255, 75, 75);	public final BooleanSetting mobs = new BooleanSetting("Mobs-Tracers", "Shows Tracers for Mobs.", false);
	public final ColorSetting mobColor = new ColorSetting("ColorForMobs-Tracers", "Tracer color for mobs.", 128, 25, 128);
	public final BooleanSetting animals = new BooleanSetting("Animals-Tracers", "Shows Tracers for Animals.", false);
	public final ColorSetting animalColor = new ColorSetting("ColorForAnimals-Tracers", "Tracer color for animals.", 75, 255, 75);
	public final BooleanSetting items = new BooleanSetting("Items-Tracers", "Shows Tracers for Items.", false);
	public final ColorSetting itemColor = new ColorSetting("ColorForItems-Tracers", "Tracer color for items.", 255, 200, 50);
	public final BooleanSetting crystals = new BooleanSetting("Crystals-Tracers", "Shows Tracers for End Crystals.", false);
	public final ColorSetting crystalColor = new ColorSetting("ColorForCrystals-Tracers", "Tracer color for crystals.", 255, 50, 255);
	public final BooleanSetting vehicles = new BooleanSetting("Vehicles-Tracers", "Shows Tracers for Vehicles (minecarts/boats).", false);
	public final ColorSetting vehicleColor = new ColorSetting("ColorForVehicles-Tracers", "Tracer color for vehicles.", 150, 150, 150);
	public final BooleanSetting armorstands = new BooleanSetting("Armorstands-Tracers", "Shows Tracers for armor stands.", false);
	public final ColorSetting armorstandColor = new ColorSetting("ColorForArmorstands-Tracers", "Outline color for armor stands.", 160, 150, 50);
	public final NumberSetting widthT = new NumberSetting("Width-Tracers", "Thickness of the tracers.", 2, 1.5, 5, 0.01);
	public final NumberSetting opacityT = new NumberSetting("Opacity-Tracers", "Opacity of the tracers.", 1, 0.75, 1, 0.01);


	public Tracers() {
		super("Tracers", "Shows lines to entities you select.", GLFW.GLFW_KEY_UNKNOWN, Category.RENDER);
		this.addSettings(players, playerColor, mobs, mobColor, animals, animalColor, items, itemColor, crystals, crystalColor, vehicles, vehicleColor, armorstands, armorstandColor, widthT, opacityT);
	}

	@HaikuSubscribe
	public void onRender(EventWorldRender.Post event) {
		float width = (float) widthT.getValue();
		int opacity = (int) (((float) opacityT.getValue()) * 255);

		for (Entity e : mc.world.getEntities()) {
			int[] col = getColor(e);

			if (col != null) {
				Vec3d vec = e.getPos().subtract(Renderer.getInterpolationOffset(e));
				Vec3d vec2 = new Vec3d(0, 0, 75)
						.rotateX(-(float) Math.toRadians(mc.gameRenderer.getCamera().getPitch()))
						.rotateY(-(float) Math.toRadians(mc.gameRenderer.getCamera().getYaw()))
						.add(mc.cameraEntity.getEyePos());

				LineColor lineColor =  LineColor.single(col[0], col[1], col[2], opacity);
				Renderer.drawLine(vec2.x, vec2.y, vec2.z, vec.x, vec.y, vec.z, lineColor, width);
				Renderer.drawLine(vec.x, vec.y, vec.z, vec.x, vec.y + e.getHeight() * 0.9, vec.z, lineColor, width);
			}
		}
	}

	private int[] getColor(Entity e) {
		if (e == mc.player)
			return null;

		if (e instanceof PlayerEntity && players.isEnabled()) {
			return players.isEnabled() ? playerColor.getRGBArray() : null;
		} else if (e instanceof Monster && mobs.isEnabled()) {
			return mobs.isEnabled() ? mobColor.getRGBArray() : null;
		} else if (EntityUtils.isAnimal(e) && animals.isEnabled()) {
			return animals.isEnabled() ? animalColor.getRGBArray() : null;
		} else if (e instanceof ItemEntity && items.isEnabled()) {
			return items.isEnabled() ? itemColor.getRGBArray() : null;
		} else if (e instanceof EndCrystalEntity && crystals.isEnabled()) {
			return crystals.isEnabled() ? crystalColor.getRGBArray() : null;
		} else if ((e instanceof BoatEntity || e instanceof AbstractMinecartEntity) && vehicles.isEnabled()) {
			return vehicles.isEnabled() ? vehicleColor.getRGBArray() : null;
		} else if (e instanceof ArmorStandEntity && armorstands.isEnabled()) {
			return armorstands.isEnabled() ? armorstandColor.getRGBArray() : null;
		}

		return null;
	}
}
