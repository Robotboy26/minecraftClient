package dev.nebula.mixin;
// package dev.vili.haiku.mixin;

// import dev.vili.haiku.Haiku;
// import dev.vili.haiku.event.events.RenderEvent;
// import net.minecraft.client.util.math.MatrixStack;

// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// @Mixin(RenderEvent.class)
// public class RenderEventMixin {

//     @Inject(method = "renderEvent", at = @At("RETURN"), cancellable = true)
//     private void renderEvent(MatrixStack content, float tickDelta, CallbackInfo ci) {
//         RenderEvent event = new RenderEvent(tickDelta, content);
//         Haiku.getInstance().getEventBus().post(event);

//         if (event.isCancelled()) ci.cancel();
//     }
// }