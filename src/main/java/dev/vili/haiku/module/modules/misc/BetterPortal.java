/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.misc;

import dev.vili.haiku.event.events.entity.player.EventClientMove;
import dev.vili.haiku.event.events.EventParticle;
import dev.vili.haiku.event.events.EventSoundPlay;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.utils.world.WorldUtils;

import org.lwjgl.glfw.GLFW;

import net.minecraft.block.Blocks;
import net.minecraft.client.particle.PortalParticle;

public class BetterPortal extends Module {
	public final BooleanSetting gui = new BooleanSetting("Gui", "Removes some of the effect of nether portals.", true);
	public final BooleanSetting overlay = new BooleanSetting("Overlay", "Removes the portal overlay.", true);
	public final BooleanSetting particles = new BooleanSetting("Particles", "Removes the portal particles that fly out of the portal.", false);
	public final BooleanSetting sound = new BooleanSetting("Sound", "Removes the portal sound when going through a nether portal.", false);
	public final BooleanSetting ambience = new BooleanSetting("Ambience", "Disables the portal ambience sound that plays when you get close to a portal.", true);

	public BetterPortal() {
		super("BetterPortal", "Removes some of the effects of nether portals.", GLFW.GLFW_KEY_9, Category.MISC);
		this.addSettings(gui, overlay, particles, sound, ambience);
	}

	@HaikuSubscribe
	public void onClientMove(EventClientMove event) {
		if (overlay.isEnabled()) {
			if (WorldUtils.doesBoxTouchBlock(mc.player.getBoundingBox(), Blocks.NETHER_PORTAL)) {
				mc.player.prevNauseaIntensity = -1f;
				mc.player.nauseaIntensity = -1f;
			}
		}
	}

	@HaikuSubscribe
	public void onParticle(EventParticle.Normal event) {
		if (particles.isEnabled() && event.getParticle() instanceof PortalParticle) {
			event.setCancelled(true);
		}
	}

	@HaikuSubscribe
	public void onSoundPlay(EventSoundPlay.Normal event) {
		if (sound.isEnabled()) {
			String path = event.getInstance().getId().getPath();
			if (path.equals("block.portal.trigger") || (ambience.isEnabled() && path.equals("block.portal.ambient"))) {
				event.setCancelled(true);
			}
		}
	}
}
