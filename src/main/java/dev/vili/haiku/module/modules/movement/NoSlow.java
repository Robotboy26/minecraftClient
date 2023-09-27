/*
 * Copyright (c) 2014-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.movement;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.NumberSetting;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffects;
import dev.vili.haiku.setting.settings.BooleanSetting;

import org.lwjgl.glfw.GLFW;

public class NoSlow extends Module
{
	public final BooleanSetting items = new BooleanSetting("Items-NoSlowHack", "Whether or not using items will slow you.", true);
	public final BooleanSetting web = new BooleanSetting("Web-NoSlowHack", "Whether or not cobwebs will not slow you down.", false);
	public final NumberSetting webTimer = new NumberSetting("WebTimer-NoSlowHack", "The timer value for WebMode Timer.", 10, 1, 10000, 1);
	public final BooleanSetting honeyBlock = new BooleanSetting("HoneyBlock-NoSlowHack", "Whether or not honey blocks will not slow you down.", true);
	public final BooleanSetting soulSand = new BooleanSetting("SoulSand-NoSlowHack", "Whether or not soul sand will not slow you down.", true);
	public final BooleanSetting slimeBlock = new BooleanSetting("SlimeBlock-NoSlowHack", "Whether or not slime blocks will not slow you down.", true);
	public final BooleanSetting berryBush = new BooleanSetting("BerryBush-NoSlowHack", "Whether or not berry bushes will not slow you down.", true);
	public final BooleanSetting airStrict = new BooleanSetting("AirStrict-NoSlowHack", "Will attempt to bypass anti-cheats like 2b2t's. Only works while in air.", false);
	public final BooleanSetting fluidDrag = new BooleanSetting("FluidDrag-NoSlowHack", "Whether or not fluid drag will not slow you down.", false);
	public final BooleanSetting sneaking = new BooleanSetting("Sneaking-NoSlowHack", "Whether or not sneaking will not slow you down.", false);
	public final BooleanSetting hunger = new BooleanSetting("Hunger-NoSlowHack", "Whether or not hunger will not slow you down.", false);
	public final BooleanSetting slowness = new BooleanSetting("Slowness-NoSlowHack", "Whether or not slowness will not slow you down.", false);

	public NoSlow()
	{
		super("NoSlow", "Do not slow down when eating.", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
	}
	
	@Override
	public void onEnable()
	{
        mc.player.removeStatusEffect(StatusEffects.SLOWNESS);
		super.onEnable();
	}
	
	@Override
	public void onDisable()
	{
		super.onDisable();
	}

    @HaikuSubscribe
    public void onTick(TickEvent event) {
        if (mc.player.hasStatusEffect(StatusEffects.SLOWNESS)) {
            mc.player.removeStatusEffect(StatusEffects.SLOWNESS);
        }
    }
	// public boolean airStrict() {
    //     return isActive() && airStrict.isEnabled() && mc.player.isUsingItem();
    // }

    // public boolean items() {
    //     return isActive() && items.isEnabled();
    // }

    // public boolean honeyBlock() {
    //     return isActive() && honeyBlock.isEnabled();
    // }

    // public boolean soulSand() {
    //     return isActive() && soulSand.isEnabled();
    // }

    // public boolean slimeBlock() {
    //     return isActive() && slimeBlock.isEnabled();
    // }

    // public boolean cobweb() {
    //     return isActive() && web.isEnabled();
    // }

    // public boolean berryBush() {
    //     return isActive() && berryBush.isEnabled();
    // }

    // public boolean fluidDrag() {
    //     return isActive() && fluidDrag.isEnabled();
    // }

    // public boolean sneaking() {
    //     return isActive() && sneaking.isEnabled();
    // }

    // public boolean hunger() {
    //     return isActive() && hunger.isEnabled();
    // }

    // public boolean slowness() {
    //     return isActive() && slowness.isEnabled();
    // }
}