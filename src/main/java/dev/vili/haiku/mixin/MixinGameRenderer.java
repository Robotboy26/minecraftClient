// package dev.vili.haiku.mixin;

// import dev.vili.haiku.Haiku;
// import dev.vili.haiku.event.events.RenderEvent;
// import net.minecraft.client.render.Camera;
// import net.minecraft.client.render.GameRenderer;
// import net.minecraft.client.util.math.MatrixStack;
// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
// import net.minecraft.client.render.LightmapTextureManager;
// import net.minecraft.client.render.GameRenderer;

// @Mixin(GameRenderer.class)
// public class MixinGameRenderer {

//     @Inject(method = "render", at = @At("HEAD"))
//     private void onRender(float tickDelta, long startTime, boolean tick, MatrixStack matrices, Camera camera, GameRenderer.LightmapTextureManager lightmapTextureManager, CallbackInfo ci) {
//         Haiku.getInstance().getEventBus().post(new RenderEvent(matrices, tickDelta));
//     }
// }
