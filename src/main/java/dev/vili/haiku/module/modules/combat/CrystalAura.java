/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.combat;

import com.google.common.collect.Streams;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.event.events.EventWorldRender;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.ColorSetting;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.utils.InventoryUtils;
import dev.vili.haiku.utils.render.Renderer;
import dev.vili.haiku.utils.render.color.QuadColor;
import dev.vili.haiku.utils.world.DamageUtils;
import dev.vili.haiku.utils.world.EntityUtils;
import dev.vili.haiku.utils.world.WorldUtils;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.lwjgl.glfw.GLFW;

// i am morbidly obese
public class CrystalAura extends Module {
	public final BooleanSetting players = new BooleanSetting("Players", "Targets players.", true);
	public final BooleanSetting mobs = new BooleanSetting("Mobs", "Targets mobs.", false);
	public final BooleanSetting animals = new BooleanSetting("Animals", "Targets animals.", false);
	public final BooleanSetting explode = new BooleanSetting("Explode", "Hits/explodes crystals.", true);
	public final BooleanSetting antiWeakness = new BooleanSetting("AntiWeakness", "Hits crystals with your strongest weapon when you have weakness.", true);
	public final BooleanSetting antiSuicide = new BooleanSetting("AntiSuicide", "Prevents you from killing yourself with a crystal.", true);
	public final NumberSetting explodeCPT = new NumberSetting("CPT", "How many crystals to hit per tick.", 2, 1, 10, 1);
	public final NumberSetting explodeCooldown = new NumberSetting("Cooldown", "How many ticks to wait before exploding the next batch of crystals.", 0, 0, 35, 1);
	public final NumberSetting explodeMinHealth = new NumberSetting("MinHealth", "Wont explode the crystal if it makes you got below the specified health.", 2, 0, 20, 0.5);
	public final BooleanSetting place = new BooleanSetting("Place", "Places crystals.", true);
	public final BooleanSetting autoSwitch = new BooleanSetting("AutoSwitch", "Automatically switches to crystal when in combat.", true);
	public final BooleanSetting switchBack = new BooleanSetting("SwitchBack", "Switches back to your previous item.", true);
	public final BooleanSetting onePointTwelvePlace = new BooleanSetting("1.12 Place", "Only places on blocks with 2 air blocks above instead of 1 because of an extra check in pre 1.13.", false);
	public final BooleanSetting blacklistB = new BooleanSetting("Blacklist", "Blacklists a crystal when it can't place so it doesn't spam packets.", true);
	public final BooleanSetting raycast = new BooleanSetting("Raycast", "Only places a crystal if you can see it.", false);
	public final NumberSetting placeMinDmg = new NumberSetting("MinDmg", "Minimum damage to the target to place crystals.", 2, 1, 20, 0.5);
	public final NumberSetting placeMinRatio = new NumberSetting("MinRatio", "Minimum damage ratio to place a crystal at (Target dmg/Player dmg).", 2, 0.5, 6, 1);
	public final NumberSetting placeCPT = new NumberSetting("CPT", "How many crystals to place per tick.", 2, 1, 10, 0);
	public final NumberSetting placeCooldownN = new NumberSetting("Cooldown", "How many ticks to wait before placing the next batch of crystals.", 5, 0, 35, 1);
	public final ColorSetting placeColor = new ColorSetting("Place Color", "The color of the block you're placing crystals on.", 178, 178, 255);
	public final BooleanSetting sameTick = new BooleanSetting("SameTick", "Enables exploding and placing crystals at the same tick.", false);
	public final BooleanSetting rotate = new BooleanSetting("Rotate", "Rotates to crystals.", false);
	public final NumberSetting rangeN = new NumberSetting("Range", "Range to place and attack crystals.", 4.5, 0, 6, 0.1);

	private BlockPos render = null;
	private int breakCooldown = 0;
	private int placeCooldown = 0;
	private Map<BlockPos, Integer> blacklist = new HashMap<>();

	public CrystalAura() {
		super("CrystalAura", "Automatically does crystalpvp for you.", GLFW.GLFW_KEY_UNKNOWN, Category.COMBAT);
		this.addSettings(players, mobs, animals, explode, antiWeakness, antiSuicide, explodeCPT, explodeCooldown, explodeMinHealth, place, autoSwitch, switchBack, onePointTwelvePlace, blacklistB, raycast, placeMinDmg, placeMinRatio, placeCPT, placeCooldownN, placeColor, sameTick, rotate, rangeN);
	}

	@HaikuSubscribe
	public void onTick(TickEvent event) {
		breakCooldown = Math.max(0, breakCooldown - 1);
		placeCooldown = Math.max(0, placeCooldown - 1);

		for (Entry<BlockPos, Integer> e : new HashMap<>(blacklist).entrySet()) {
			if (e.getValue() > 0) {
				blacklist.replace(e.getKey(), e.getValue() - 1);
			} else {
				blacklist.remove(e.getKey());
			}
		}

		if (mc.player.isUsingItem() && mc.player.getMainHandStack().isFood()) {
			return;
		}

		List<LivingEntity> targets = Streams.stream(mc.world.getEntities())
				.filter(e -> EntityUtils.isAttackable(e, true))
				.filter(e -> (players.isEnabled() && EntityUtils.isPlayer(e))
						|| (mobs.isEnabled() && EntityUtils.isMob(e))
						|| (animals.isEnabled() && EntityUtils.isAnimal(e)))
				.map(e -> (LivingEntity) e)
				.toList();

		if (targets.isEmpty()) {
			return;
		}

		// Explode
		Boolean explodeToggle = explode.isEnabled();
		List<EndCrystalEntity> nearestCrystals = Streams.stream(mc.world.getEntities())
				.filter(e -> e instanceof EndCrystalEntity)
				.map(e -> (EndCrystalEntity) e)
				.sorted(Comparator.comparing(mc.player::distanceTo))
				.toList();

		int breaks = 0;
		if (explodeToggle && !nearestCrystals.isEmpty() && breakCooldown <= 0) {
			boolean end = false;
			for (EndCrystalEntity c : nearestCrystals) {
				if (mc.player.distanceTo(c) > rangeN.getValue()) {
					mc.world.getOtherEntities(null, new Box(c.getPos(), c.getPos()).expand(7), targets::contains).isEmpty();
					continue;
				}

				float damage = DamageUtils.getExplosionDamage(c.getPos(), 6f, mc.player);
				if (DamageUtils.willGoBelowHealth(mc.player, damage, (int) explodeMinHealth.getValue())) {
					continue;
				}

				int oldSlot = mc.player.getInventory().selectedSlot;
				if (antiWeakness.isEnabled() && mc.player.hasStatusEffect(StatusEffects.WEAKNESS)) {
					InventoryUtils.selectSlot(false, true, Comparator.comparing(i -> DamageUtils.getItemAttackDamage(mc.player.getInventory().getStack(i))));
				}

				if (rotate.isEnabled()) {
					Vec3d eyeVec = mc.player.getEyePos();
					Vec3d v = new Vec3d(c.getX(), c.getY() + 0.5, c.getZ());
					for (Direction d : Direction.values()) {
						Vec3d vd = WorldUtils.getLegitLookPos(c.getBoundingBox(), d, true, 5, -0.001);
						if (vd != null && eyeVec.distanceTo(vd) <= eyeVec.distanceTo(v)) {
							v = vd;
						}
					}
					if(rotate.isEnabled()) {
						WorldUtils.facePos(v.x, v.y, v.z);
					}
				}

				mc.interactionManager.attackEntity(mc.player, c);
				mc.player.swingHand(Hand.MAIN_HAND);
				blacklist.remove(c.getBlockPos().down());

				InventoryUtils.selectSlot(oldSlot);

				end = true;
				breaks++;
				if (breaks >= explodeCPT.getValue()) {
					break;
				}
			}

			breakCooldown = (int) explodeCooldown.getValue() + 1;

			if (!sameTick.isEnabled() && end) {
				return;
			}
		}

		// Place
		boolean placeToggle = place.isEnabled();
		if (placeToggle && placeCooldown <= 0) {
			int crystalSlot = !autoSwitch.isEnabled()
					? (mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL ? mc.player.getInventory().selectedSlot
							: mc.player.getOffHandStack().getItem() == Items.END_CRYSTAL ? 40
									: -1)
							: InventoryUtils.getSlot(true, i -> mc.player.getInventory().getStack(i).getItem() == Items.END_CRYSTAL);

			if (crystalSlot == -1) {
				return;
			}

			Map<BlockPos, Float> placeBlocks = new LinkedHashMap<>();

			for (Vec3d v : getCrystalPoses()) {
				float playerDamg = DamageUtils.getExplosionDamage(v, 6f, mc.player);

				if (DamageUtils.willKill(mc.player, playerDamg))
					continue;

				for (LivingEntity e : targets) {
					float targetDamg = DamageUtils.getExplosionDamage(v, 6f, e);
					if (DamageUtils.willPop(mc.player, playerDamg) && !DamageUtils.willPopOrKill(e, targetDamg)) {
						continue;
					}

					if (targetDamg >= placeMinDmg.getValue()) {
						float ratio = playerDamg == 0 ? targetDamg : targetDamg / playerDamg;

						if (ratio > placeMinRatio.getValue()) {
							placeBlocks.put(BlockPos.ofFloored(v).down(), ratio);
						}
					}
				}
			}

			placeBlocks = placeBlocks.entrySet().stream()
					.sorted((b1, b2) -> Float.compare(b2.getValue(), b1.getValue()))
					.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (x, y) -> y, LinkedHashMap::new));

			int oldSlot = mc.player.getInventory().selectedSlot;
			int places = 0;
			for (Entry<BlockPos, Float> e : placeBlocks.entrySet()) {
				BlockPos block = e.getKey();

				Vec3d eyeVec = mc.player.getEyePos();

				Vec3d vec = Vec3d.ofCenter(block, 1);
				Direction dir = null;
				for (Direction d : Direction.values()) {
					Vec3d vd = WorldUtils.getLegitLookPos(block, d, true, 5);
					if (vd != null && eyeVec.distanceTo(vd) <= eyeVec.distanceTo(vec)) {
						vec = vd;
						dir = d;
					}
				}

				if (dir == null) {
					if (raycast.isEnabled())
						continue;

					dir = Direction.UP;
				}

				if (blacklistB.isEnabled())
					blacklist.put(block, 4);

				if (rotate.isEnabled()) {
					WorldUtils.facePos(vec.x, vec.y, vec.z);
				}

				Hand hand = InventoryUtils.selectSlot(crystalSlot);

				render = block;
				mc.interactionManager.interactBlock(mc.player, hand, new BlockHitResult(vec, dir, block, false));

				places++;
				if (places >= placeCPT.getValue()) {
					break;
				}
			}

			if (places > 0) {
				if (autoSwitch.isEnabled() && switchBack.isEnabled()) {
					InventoryUtils.selectSlot(oldSlot);
				}

				placeCooldown = (int) placeCooldownN.getValue() + 1;
			}
		}
	}

	@HaikuSubscribe
	public void onRenderWorld(EventWorldRender.Post event) {
		if (this.render != null) {
			int[] col = placeColor.getRGBArray();
			Renderer.drawBoxBoth(render, QuadColor.single(col[0], col[1], col[2], 100), 2.5f);
		}
	}

	public Set<Vec3d> getCrystalPoses() {
		Set<Vec3d> poses = new HashSet<>();

		int range = (int) Math.floor(rangeN.getValue());
		for (int x = -range; x <= range; x++) {
			for (int y = -range; y <= range; y++) {
				for (int z = -range; z <= range; z++) {
					BlockPos basePos = BlockPos.ofFloored(mc.player.getEyePos()).add(x, y, z);

					if (!canPlace(basePos) || (blacklist.containsKey(basePos) && blacklist.get(basePos) > 0))
						continue;

					if (onePointTwelvePlace.isEnabled()) {
						boolean allBad = true;
						for (Direction d : Direction.values()) {
							if (WorldUtils.getLegitLookPos(basePos, d, true, 5) != null) {
								allBad = false;
								break;
							}
						}

						if (allBad) {
							continue;
						}
					}

					if (mc.player.getPos().distanceTo(Vec3d.of(basePos).add(0.5, 1, 0.5)) <= rangeN.getValue() + 0.25)
						poses.add(Vec3d.of(basePos).add(0.5, 1, 0.5));
				}
			}
		}

		return poses;
	}

	private boolean canPlace(BlockPos basePos) {
		BlockState baseState = mc.world.getBlockState(basePos);

		if (baseState.getBlock() != Blocks.BEDROCK && baseState.getBlock() != Blocks.OBSIDIAN)
			return false;

		boolean oldPlace = antiSuicide.isEnabled();
		BlockPos placePos = basePos.up();
		if (!mc.world.isAir(placePos) || (oldPlace && !mc.world.isAir(placePos.up())))
			return false;

		return mc.world.getOtherEntities(null, new Box(placePos, placePos.up(oldPlace ? 2 : 1))).isEmpty();
	}
}
