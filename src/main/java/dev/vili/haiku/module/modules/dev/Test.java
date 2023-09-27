package dev.vili.haiku.module.modules.dev;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.client.render.RenderLayer;

import org.lwjgl.glfw.GLFW;

public class Test extends Module 
{
    public Test() {
        super("test", "test", GLFW.GLFW_KEY_UNKNOWN, Category.DEV);
    }

    @Override
    public void onEnable() {
        BlockPos pos = mc.player.getBlockPos();
        Box box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
        MatrixStack matrixStack = new MatrixStack();
        VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer vertexConsumer = immediate.getBuffer(RenderLayer.getLines());
        WorldRenderer.drawBox(matrixStack, vertexConsumer, box, 1, 1, 1, 1);
        immediate.draw();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @HaikuSubscribe
    private void onTick(TickEvent event) {
    }
}
