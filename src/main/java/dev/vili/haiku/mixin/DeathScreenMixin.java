// /*
//  * Copyright (c) 2014-2023 Wurst-Imperium and contributors.
//  *
//  * This source code is subject to the terms of the GNU General Public
//  * License, version 3. If a copy of the GPL was not distributed with this
//  * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
//  */
// package dev.vili.haiku.mixin;

// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// import net.minecraft.client.gui.screen.DeathScreen;
// import net.minecraft.client.gui.screen.Screen;
// import net.minecraft.text.Text;
// import dev.vili.haiku.Haiku;

// @Mixin(DeathScreen.class)
// public abstract class DeathScreenMixin extends Screen
// {
// 	private DeathScreenMixin(Haiku wurst, Text title)
// 	{
// 		super(title);
// 	}
	
// 	@Inject(at = @At(value = "TAIL"), method = "tick()V")
// 	private void onTick(CallbackInfo ci)
// 	{
// 		Haiku.getInstance().getEventBus().post(DeathEvent);
// 		EventManager.fire(DeathEvent.INSTANCE);
// 	}
// }
