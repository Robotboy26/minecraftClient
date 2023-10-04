// /*
//  * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
//  * Copyright (c) 2021 Bleach and contributors.
//  *
//  * This source code is subject to the terms of the GNU General Public
//  * License, version 3. If a copy of the GPL was not distributed with this
//  * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
//  */
// package dev.vili.haiku.module.modules.player;

// import java.util.Set;

// import org.lwjgl.glfw.GLFW;

// import dev.vili.haiku.event.events.TickEvent;
// import dev.vili.haiku.eventbus.HaikuSubscribe;
// import dev.vili.haiku.module.Module;
// import dev.vili.haiku.setting.settings.ModeSetting;
// import dev.vili.haiku.setting.settings.BooleanSetting;
// import dev.vili.haiku.setting.settings.NumberSetting;

// import com.google.common.collect.Sets;

// import net.minecraft.item.Item;
// import net.minecraft.item.Items;

// public class FastUse extends Module {
// 	public final ModeSetting mode = new ModeSetting("Mode-FastUse", "FastUse mode.", "Single", "Multi");
// 	public final NumberSetting multi = new NumberSetting("Multi-FastUse", "How many items to use per tick if on multi mode.", 20, 1, 100, 1);
// 	public final BooleanSetting throwablesOnly = new BooleanSetting("ThrowablesOnly-FastUse", "Only uses throwables.", true);
// 	public final BooleanSetting xpOnly = new BooleanSetting("XPOnly-FastUse", "Only uses XP bottles.", false);

// 	private static final Set<Item> THROWABLE = Sets.newHashSet(
// 			Items.SNOWBALL, Items.EGG, Items.EXPERIENCE_BOTTLE,
// 			Items.ENDER_EYE, Items.ENDER_PEARL, Items.SPLASH_POTION, Items.LINGERING_POTION);

// 	public FastUse() {
// 		super("FastUse", "Allows you to use items faster.", GLFW.GLFW_KEY_UNKNOWN, Category.PLAYER);

// 		super("FastUse", KEY_UNBOUND, ModuleCategory.PLAYER, "Allows you to use items faster.",
// 				new SettingMode("Mode", "Single", "Multi").withDesc("Whether to throw once per tick or multiple times."),
// 				new SettingSlider("Multi", 1, 100, 20, 0).withDesc("How many items to use per tick if on multi mode."),
// 				new SettingToggle("Throwables Only", true).withDesc("Only uses throwables.").withChildren(
// 						new SettingToggle("XP Only", false).withDesc("Only uses XP bottles.")));
// 	}

// 	@HaikuSubscribe
// 	public void onTick(TickEvent event) {
// 		if (throwablesOnly.isEnabled()) {
// 			if (!(THROWABLE.contains(mc.player.getMainHandStack().getItem())
// 					&& (!xpOnly.isEnabled()
// 							|| mc.player.getMainHandStack().getItem() == Items.EXPERIENCE_BOTTLE))) {
// 				return;
// 			}
// 		}

// 		mc.itemUseCooldown = 0;
// 		if (mode.getMode() == "Multi" && mc.options.useKey.isPressed()) {
// 			for (int i = 0; i < multi.getValue(); i++) {
// 				mc.doItemUse();
// 			}
// 		}
// 	}
// }
