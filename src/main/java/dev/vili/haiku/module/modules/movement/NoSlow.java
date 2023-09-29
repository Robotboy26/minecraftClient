/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.movement;

import dev.vili.haiku.event.events.entity.player.EventClientMove;
import dev.vili.haiku.event.events.EventPacket;
import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.event.events.EventWorldRender;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.utils.world.WorldUtils;
import org.lwjgl.glfw.GLFW;

import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.JigsawBlockScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.gui.screen.ingame.StructureBlockScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.tick.Tick;

public class NoSlow extends Module {
    public final BooleanSetting slowness = new BooleanSetting("Slowness-NoSlow", "Removes the slowness effect.", true);
    public final BooleanSetting soulSand = new BooleanSetting("SoulSand-NoSlow", "Removes soul sand slowness.", true);
    public final BooleanSetting slimeBlocks = new BooleanSetting("SlimeBlocks-NoSlow", "Removes slimeblock slowness.", true);
    public final BooleanSetting webs = new BooleanSetting("Webs-NoSlow", "Removes cobweb slowness.", true);
    public final BooleanSetting berryBush = new BooleanSetting("Berry Bush-NoSlow", "Removes berry bush slowness.", true);
    public final BooleanSetting items = new BooleanSetting("Items-NoSlow", "Removes the slowness while eating items.", true);
    public final BooleanSetting inventory = new BooleanSetting("Inventory-NoSlow", "Allows you to move while in inventories.", true);
    public final BooleanSetting sneak = new BooleanSetting("Sneaking-NoSlow", "Enables the sneak key while in inventories.", false);
    public final BooleanSetting ncpBypass = new BooleanSetting("NCPBypass-NoSlow", "Allows you to move items around on serves with NCP.", false);
    public final BooleanSetting rotate = new BooleanSetting("Rotate-NoSlow", "Allows you to use the arrow keys to rotate.", true);
    public final BooleanSetting pitchLimit = new BooleanSetting("PitchLimit-NoSlow", "Prevents you from being able to do a 360 pitch spin.", true);
    public final BooleanSetting antiSpinbot = new BooleanSetting("Anti-Spinbot-NoSlow", "Adds a random amount of rotation when spinning to prevent spinbot detects.", true);

	private Vec3d addVelocity = Vec3d.ZERO;
	private long lastTime;

	public NoSlow() {
        super("NoSlow", "Disables Stuff From Slowing You Down.", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
        this.addSettings(slowness, soulSand, slimeBlocks, webs, berryBush, items, inventory, sneak, ncpBypass, rotate, pitchLimit, antiSpinbot);
	}

	@HaikuSubscribe
	public void onClientMove(EventClientMove event) {
		if (!isEnabled())
			return;

		/* Slowness */
		if (slowness.isEnabled() && (mc.player.getStatusEffect(StatusEffects.SLOWNESS) != null || mc.player.getStatusEffect(StatusEffects.BLINDNESS) != null)) {
			if (mc.options.forwardKey.isPressed()
					&& mc.player.getVelocity().x > -0.15 && mc.player.getVelocity().x < 0.15
					&& mc.player.getVelocity().z > -0.15 && mc.player.getVelocity().z < 0.15) {
				mc.player.setVelocity(mc.player.getVelocity().add(addVelocity));
				addVelocity = addVelocity.add(new Vec3d(0, 0, 0.05).rotateY(-(float) Math.toRadians(mc.player.getYaw())));
			} else {
				addVelocity = addVelocity.multiply(0.75, 0.75, 0.75);
			}
		}

		/* Soul Sand */
		if (soulSand.isEnabled() && mc.world.getBlockState(mc.player.getBlockPos()).getBlock() == Blocks.SOUL_SAND) {
			mc.player.setVelocity(mc.player.getVelocity().multiply(2.5, 1, 2.5));
		}

		/* Slime Block */
		if (slimeBlocks.isEnabled()
				&& mc.world.getBlockState(BlockPos.ofFloored(mc.player.getPos().add(0, -0.01, 0))).getBlock() == Blocks.SLIME_BLOCK && mc.player.isOnGround()) {
			double d = Math.abs(mc.player.getVelocity().y);
			if (d < 0.1D && !mc.player.bypassesSteppingEffects()) {
				double e = 1 / (0.4D + d * 0.2D);
				mc.player.setVelocity(mc.player.getVelocity().multiply(e, 1.0D, e));
			}
		}

		/* Web */
		if (webs.isEnabled() && WorldUtils.doesBoxTouchBlock(mc.player.getBoundingBox(), Blocks.COBWEB)) {
			// still kinda scuffed until i get an actual mixin
			mc.player.slowMovement(mc.world.getBlockState(mc.player.getBlockPos()), new Vec3d(1.75, 1.75, 1.75));
		}

		/* Berry Bush */
		if (berryBush.isEnabled() && WorldUtils.doesBoxTouchBlock(mc.player.getBoundingBox(), Blocks.SWEET_BERRY_BUSH)) {
			// also scuffed
			mc.player.slowMovement(mc.world.getBlockState(mc.player.getBlockPos()), new Vec3d(1.7, 1.7, 1.7));
		}

		// Items handled in MixinPlayerEntity:sendMovementPackets_isUsingItem
	}

	@HaikuSubscribe
	public void onTick(TickEvent event) {
		/* Inventory */
		if (inventory.isEnabled() && shouldInvMove(mc.currentScreen)) {

			for (KeyBinding k : new KeyBinding[] { mc.options.forwardKey, mc.options.backKey,
					mc.options.leftKey, mc.options.rightKey, mc.options.jumpKey, mc.options.sprintKey }) {
				k.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(),
						InputUtil.fromTranslationKey(k.getBoundKeyTranslationKey()).getCode()));
			}

			if (sneak.isEnabled()) {
				mc.options.sneakKey.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(),
						InputUtil.fromTranslationKey(mc.options.sneakKey.getBoundKeyTranslationKey()).getCode()));
			}


		}
	}


	@HaikuSubscribe
	public void onRender(EventWorldRender.Post event) {
		/* Inventory */
		if (inventory.isEnabled()
				&& rotate.isEnabled()
				&& shouldInvMove(mc.currentScreen)) {

			float yaw = 0f;
			float pitch = 0f;

			//mc.keyboard.setRepeatEvents(true);

			float amount = (System.currentTimeMillis() - lastTime) / 10f;
			lastTime = System.currentTimeMillis();

			if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT))
				yaw -= amount;
			if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT))
				yaw += amount;
			if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_UP))
				pitch -= amount;
			if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_DOWN))
				pitch += amount;

			if (antiSpinbot.isEnabled()) {
				if (yaw == 0f && pitch != 0f) {
					yaw += -0.1 + Math.random() / 5f;
				} else {
					yaw *= 0.75f + Math.random() / 2f;
				}

				if (pitch == 0f && yaw != 0f) {
					pitch += -0.1 + Math.random() / 5f;
				} else {
					pitch *= 0.75f + Math.random() / 2f;
				}
			}


			mc.player.setYaw(mc.player.getYaw() + yaw);

			if (pitchLimit.isEnabled()) {
				mc.player.setPitch(MathHelper.clamp(mc.player.getPitch() + pitch, -90f, 90f));
			} else {
				mc.player.setPitch(mc.player.getPitch() + pitch);
			}
		}
	}

	@HaikuSubscribe
	public void onSendPacket(EventPacket.Send event) {
		if (event.getPacket() instanceof ClickSlotC2SPacket && pitchLimit.isEnabled()) {
			mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.STOP_SPRINTING));
		}
	}

	private boolean shouldInvMove(Screen screen) {
		if (screen == null) {
			return false;
		}

		return !(screen instanceof ChatScreen
				|| screen instanceof BookEditScreen
				|| screen instanceof SignEditScreen
				|| screen instanceof JigsawBlockScreen
				|| screen instanceof StructureBlockScreen
				|| screen instanceof AnvilScreen
				|| screen instanceof CreativeInventoryScreen);
	}
}
