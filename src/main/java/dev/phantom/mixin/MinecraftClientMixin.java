/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.phantom.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.phantom.Phantom;
import dev.phantom.mixinterface.IMinecraftClient;
import net.minecraft.client.RunArgs;

@Mixin(value = MinecraftClient.class, priority = 1001)
public abstract class MinecraftClientMixin implements IMinecraftClient {
    @Unique private boolean doItemUseCalled;
    @Unique private boolean rightClick;
    @Unique private long lastTime;
    @Unique private boolean firstFrame;

    @Shadow public ClientWorld world;
    @Shadow @Final public Mouse mouse;
    @Shadow @Final private Window window;
    @Shadow public Screen currentScreen;
    @Shadow @Final public GameOptions options;

    @Shadow protected abstract void doItemUse();
    @Shadow public abstract Profiler getProfiler();
    @Shadow public abstract boolean isWindowFocused();

    @Shadow
    @Nullable
    public ClientPlayerInteractionManager interactionManager;

    @Shadow
    private int itemUseCooldown;

    @Inject(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/MinecraftClient;setOverlay(Lnet/minecraft/client/gui/screen/Overlay;)V", shift = At.Shift.BEFORE))
    private void init(RunArgs args, CallbackInfo ci) {
        Phantom.getInstance().onInitialize();
    }

    // set the game window title
    @Inject(at = @At(value = "RETURN"), method = "getWindowTitle", cancellable = true)
	private void getWindowTitle(final CallbackInfoReturnable<String> info)
	{
		StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Phantom.MOD_NAME + " " + Phantom.MOD_VERSION);
		info.setReturnValue(stringBuilder.toString());
	}

    // @Inject(at = @At("HEAD"), method = "tick")
    // private void onPreTick(CallbackInfo info) {
    //     OnlinePlayers.update();

    //     doItemUseCalled = false;

    //     if (rightClick && !doItemUseCalled && interactionManager != null) doItemUse();
    //     rightClick = false;
    // }

    // @Inject(at = @At("TAIL"), method = "tick")
    // private void onTick(CallbackInfo info) {
    // }

    // @Inject(method = "doAttack", at = @At("HEAD"))
    // private void onAttack(CallbackInfoReturnable<Boolean> cir) {
    //     CPSUtils.onAttack();
    // }

    // @Inject(method = "doItemUse", at = @At("HEAD"))
    // private void onDoItemUse(CallbackInfo info) {
    //     doItemUseCalled = true;
    // }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("HEAD"))
    private void onDisconnect(Screen screen, CallbackInfo info) {
        // Phantom.getInstance().getModuleManager().getModule("Freecam").setEnabled(false);
        // Phantom.getInstance().getModuleManager().getModule("LogoutTimer").setEnabled(false);
    }

    // @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    // private void onSetScreen(Screen screen, CallbackInfo info) {
    //     if (screen instanceof WidgetScreen) screen.mouseMoved(mouse.getX() * window.getScaleFactor(), mouse.getY() * window.getScaleFactor());

    //     OpenScreenEvent event = OpenScreenEvent.get(screen);
    //     Haiku.getInstance().getEventBus().post(event);

    //     if (event.isCancelled()) info.cancel();
    // }

    // @Inject(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isItemEnabled(Lnet/minecraft/resource/featuretoggle/FeatureSet;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    // private void onDoItemUseHand(CallbackInfo ci, Hand[] var1, int var2, int var3, Hand hand, ItemStack itemStack) {
    //     FastUse fastUse = Modules.get().get(FastUse.class);
    //     if (fastUse.isActive()) {
    //         itemUseCooldown = fastUse.getItemUseCooldown(itemStack);
    //     }
    // }

    // @ModifyExpressionValue(method = "doItemUse", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;crosshairTarget:Lnet/minecraft/util/hit/HitResult;", ordinal = 1))
    // private HitResult doItemUseMinecraftClientCrosshairTargetProxy(HitResult original) {
    //     return Haiku.getInstance().getEventBus().post(ItemUseCrosshairTargetEvent.get(original)).target;
    // }

    // @ModifyReturnValue(method = "reloadResources(Z)Ljava/util/concurrent/CompletableFuture;", at = @At("RETURN"))
    // private CompletableFuture<Void> onReloadResourcesNewCompletableFuture(CompletableFuture<Void> original) {
    //     return original.thenRun(() -> Haiku.getInstance().getEventBus().post(ResourcePacksReloadedEvent.get()));
    // }

    // @ModifyArg(method = "updateWindowTitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setTitle(Ljava/lang/String;)V"))
    // private String setTitle(String original) {
    //     if (Config.get() == null || !Config.get().customWindowTitle.get()) return original;

    //     String customTitle = Config.get().customWindowTitleText.get();
    //     Script script = MeteorStarscript.compile(customTitle);

    //     if (script != null) {
    //         String title = MeteorStarscript.run(script);
    //         if (title != null) customTitle = title;
    //     }

    //     return customTitle;
    // }

    // @Inject(method = "onResolutionChanged", at = @At("TAIL"))
    // private void onResolutionChanged(CallbackInfo info) {
    //     Haiku.getInstance().getEventBus().post(WindowResizedEvent.get());
    // }

    // @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
    // private void onGetFramerateLimit(CallbackInfoReturnable<Integer> info) {
    //     if (Modules.get().isActive(UnfocusedCPU.class) && !isWindowFocused()) info.setReturnValue(Math.min(Modules.get().get(UnfocusedCPU.class).fps.get(), this.options.getMaxFps().getValue()));
    // }

    // Time delta

    // @Inject(method = "render", at = @At("HEAD"))
    // private void onRender(CallbackInfo info) {
    //     long time = System.currentTimeMillis();

    //     if (firstFrame) {
    //         lastTime = time;
    //         firstFrame = false;
    //     }

    //     Utils.frameTime = (time - lastTime) / 1000.0;
    //     lastTime = time;
    // }

    // Interface

    // @Override
    // public void rightClick() {
    //     rightClick = true;
    // }
}
