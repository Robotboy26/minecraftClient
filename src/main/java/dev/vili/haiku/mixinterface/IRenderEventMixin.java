package dev.vili.haiku.mixinterface;

import dev.vili.haiku.Haiku;
import dev.vili.haiku.eventbus.EventBus;
import dev.vili.haiku.event.events.Render3DEvent;
import dev.vili.haiku.render.Renderer3D;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

    @Mixin(GameRenderer.class)
    public class IRenderEventMixin {
        @Inject(method = "render", at = @At("HEAD"))
        private void onRender(float tickDelta, long startTime, boolean tick, Camera camera, MatrixStack matrixStack, CallbackInfo ci) {
            MinecraftClient.getInstance().getProfiler().push("haiku_render_pre");
            Renderer3D renderer3D = Haiku.getInstance().getRenderManager(Renderer3D.class);
            Render3DEvent preEvent = Render3DEvent.get(matrixStack, renderer3D, tickDelta, camera.getPos().x, camera.getPos().y, camera.getPos().z);
            Haiku.getInstance().post(preEvent);
            MinecraftClient.getInstance().getProfiler().pop();
        }

        @Inject(method = "render", at = @At("RETURN"))
        private void afterRender(float tickDelta, long startTime, boolean tick, Camera camera, MatrixStack matrixStack, CallbackInfo ci) {
            MinecraftClient.getInstance().getProfiler().push("haiku_render_post");
            Renderer3D renderer3D = Haiku.getInstance().getRenderManager(Renderer3D.class);
            Render3DEvent postEvent = Render3DEvent.get(matrixStack, renderer3D, tickDelta, camera.getPos().x, camera.getPos().y, camera.getPos().z);
            HaikuEventBus.getInstance().post(postEvent);
            MinecraftClient.getInstance().getProfiler().pop();
        }
    }