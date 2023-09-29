/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.vili.haiku.module.modules.movement;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.event.events.EventWorldRender;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.ColorSetting;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.utils.render.Renderer;
import dev.vili.haiku.utils.render.color.QuadColor;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ClickTP extends Module {
    public final BooleanSetting inAir = new BooleanSetting("InAir", "Teleports even if you are pointing in the air.", true);
    public final BooleanSetting liquids = new BooleanSetting("Liquids", "Interacts with liquids.", false);
    public final BooleanSetting yFirst = new BooleanSetting("YFirst", "Sets you to the correct Y level first, then to your XZ coords, might fix going through walls.", false);
    public final BooleanSetting alwaysUp = new BooleanSetting("AlwaysUp", "Always teleports you to the top of blocks instead of sides.", false);
    public final ColorSetting highlight = new ColorSetting("Highlight", "The color of the target block.", 128, 50, 200);

	private BlockPos pos = null;
	private Direction dir = null;

	private boolean antiSpamClick = false;

	public ClickTP() {
        super("ClickTP", "Allows you to teleport by clicking.", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
        this.addSettings(inAir, liquids, yFirst, alwaysUp, highlight);
	}

    @Override
    public void onEnable() {
        super.onEnable();
    }

	@Override
	public void onDisable() {
		pos = null;
		dir = null;

		super.onDisable();
	}

	@HaikuSubscribe
	public void onWorldRender(EventWorldRender event) {
		if (pos != null && dir != null) {
			int[] col = highlight.getRGBArray();
			Renderer.drawBoxBoth(new Box(
					pos.getX() + (dir == Direction.EAST ? 0.95 : 0), pos.getY() + (dir == Direction.UP ? 0.95 : 0), pos.getZ() + (dir == Direction.SOUTH ? 0.95 : 0),
					pos.getX() + (dir == Direction.WEST ? 0.05 : 1), pos.getY() + (dir == Direction.DOWN ? 0.05 : 1), pos.getZ() + (dir == Direction.NORTH ? 0.05 : 1)),
					QuadColor.single(col[0], col[1], col[2], 128), 2.5f);
		}
	}

	@HaikuSubscribe
	public void onTick(TickEvent event) {
		if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			pos = null;
			dir = null;
			return;
		}

		BlockHitResult hit = (BlockHitResult) mc.player.raycast(100, mc.getTickDelta(), liquids.isEnabled());

		boolean miss = hit.getType() == Type.MISS && !inAir.isEnabled();

		pos = miss ? null : hit.getBlockPos();
		dir = miss ? null : alwaysUp.isEnabled() ? Direction.UP : hit.getSide();

		if (pos != null && dir != null) {
			if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == 1 && mc.currentScreen == null && !antiSpamClick) {
				antiSpamClick = true;

				Vec3d tpPos = Vec3d.ofBottomCenter(pos.offset(dir, dir == Direction.DOWN ? 2 : 1));

				if (yFirst.isEnabled()) {
					mc.player.updatePosition(mc.player.getX(), tpPos.y, mc.player.getZ());
					mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), tpPos.y, mc.player.getZ(), false));
				}

				mc.player.updatePosition(tpPos.x, tpPos.y, tpPos.z);
				mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(tpPos.x, tpPos.y, tpPos.z, false));
			} else if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == 0) {
				antiSpamClick = false;
			}
		}
	}
}
