package dev.vili.haiku.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;

import dev.vili.haiku.utils.HaikuLogger;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;

import dev.vili.haiku.Haiku;
import dev.vili.haiku.MixinResources.mixinterface.IMultiplayerScreen;
import dev.vili.haiku.MixinResources.serverfinder.CleanUpScreen;
import dev.vili.haiku.MixinResources.serverfinder.ServerFinderScreen;
import dev.vili.haiku.MixinResources.serverfinder.LastServerRememberer;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin extends Screen implements IMultiplayerScreen {

	protected MultiplayerScreenMixin(Text title) {
		super(title);
	}

	@Inject(at = {@At("TAIL")}, method = {"init()V"})
	private void onInit(CallbackInfo ci) {
		this.addDrawableChild(ButtonWidget.builder(Text.of("this is a button"), b -> HaikuLogger.info("hellowofosoef"))
		 		.dimensions(this.width / 2 + 4 + 50, 7, 100, 20).build());
			
		lastServerButton = addDrawableChild(ButtonWidget
			.builder(Text.literal("Last Server"),
				b -> LastServerRememberer
					.joinLastServer((MultiplayerScreen)(Object)this))
			.dimensions(width / 2 - 154, 10, 100, 20).build());
		
		addDrawableChild(
			ButtonWidget
				.builder(Text.literal("Server Finder"),
					b -> client.setScreen(new ServerFinderScreen(
						(MultiplayerScreen)(Object)this)))
				.dimensions(width / 2 + 154 + 4, height - 54, 100, 20).build());
		
		addDrawableChild(ButtonWidget
			.builder(Text.literal("Clean Up"),
				b -> client.setScreen(
					new CleanUpScreen((MultiplayerScreen)(Object)this)))
			.dimensions(width / 2 + 154 + 4, height - 30, 100, 20).build());
	}
	
	@Shadow
	protected MultiplayerServerListWidget serverListWidget;
	
	private ButtonWidget lastServerButton;
	
	private MultiplayerScreenMixin(Haiku wurst, Text title)
	{
		super(title);
	}
		
	
	@Inject(at = @At("TAIL"), method = "tick()V")
	private void onTick(CallbackInfo ci)
	{
		if(lastServerButton == null)
			return;
		
		lastServerButton.active = LastServerRememberer.getLastServer() != null;
	}
	
	@Inject(at = @At("HEAD"),
		method = "connect(Lnet/minecraft/client/network/ServerInfo;)V")
	private void onConnect(ServerInfo entry, CallbackInfo ci)
	{
		LastServerRememberer.setLastServer(entry);
	}
	
	@Override
	public MultiplayerServerListWidget getServerListSelector()
	{
		return serverListWidget;
	}
	
	@Override
	public void connectToServer(ServerInfo server)
	{
		connect(server);
	}
	
	@Shadow
	private void connect(ServerInfo entry)
	{
		
	}
}