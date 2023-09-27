/*
 * Copyright (c) 2014-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.player;

import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import net.minecraft.entity.effect.StatusEffects;

import org.lwjgl.glfw.GLFW;

public final class AntiWither extends Module 
{
    public AntiWither() {
        super("AntiWither", "No Wither effect.", GLFW.GLFW_KEY_PAGE_DOWN, Category.RENDER);
    }

    @Override
    public void onEnable() {
        mc.player.removeStatusEffect(StatusEffects.WITHER);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @HaikuSubscribe
    public void onTick() {
        if (mc.player.hasStatusEffect(StatusEffects.WITHER)) {
            mc.player.removeStatusEffect(StatusEffects.WITHER);
        }
    }
}