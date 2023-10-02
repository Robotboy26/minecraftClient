// package dev.vili.haiku.module.modules.dev;

// import java.util.ArrayList;
// import java.util.List;

// import org.joml.Matrix4f;
// import org.joml.Quaternionf;
// import org.joml.Vector3f;
// import org.lwjgl.glfw.GLFW;

// import dev.vili.haiku.event.events.RenderEvent;
// import dev.vili.haiku.eventbus.HaikuSubscribe;
// import dev.vili.haiku.module.Module;
// import net.minecraft.block.Blocks;
// import net.minecraft.block.ChestBlock;
// import net.minecraft.client.MinecraftClient;
// import net.minecraft.client.render.VertexConsumer;
// import net.minecraft.client.render.VertexConsumerProvider;
// import net.minecraft.client.render.block.BlockRenderManager;
// import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
// import net.minecraft.client.util.math.MatrixStack;
// import net.minecraft.util.math.BlockPos;
// import net.minecraft.util.math.Box;
// import net.minecraft.world.World;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
// import net.minecraft.text.Text;
// import net.minecraft.client.render.RenderLayer;
// import net.minecraft.client.render.VertexConsumerProvider;

// public class Test extends Module {
//     private final BlockPos pos = new BlockPos(0, 64, 0);

//     public Test() {
//         super("test", "test", GLFW.GLFW_KEY_UNKNOWN, Category.DEV);
//     }

//     @Override
//     public void onEnable() {
//         super.onEnable();
//         MinecraftClient.getInstance().world.setBlockState(pos, Blocks.CHEST.getDefaultState());
//     }

//     @Override
//     public void onDisable() {
//         super.onDisable();
//         MinecraftClient.getInstance().world.setBlockState(pos, Blocks.AIR.getDefaultState());
//     }

//     @HaikuSubscribe
//     public void onRender(RenderEvent event) {
//         MinecraftClient mc = MinecraftClient.getInstance();
//         World world = mc.world;

//         MatrixStack matrixStack = event.getMatrixStack();
//         VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();
//         Box box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);

//         mc.getBlockRenderManager().renderBlockAsEntity(Blocks.CHEST.getDefaultState(), matrixStack, immediate, 0, 0);
//         mc.getBlockRenderManager().getModelRenderer().render(world, mc.getBlockRenderManager().getModel(Blocks.CHEST.getDefaultState()), Blocks.CHEST.getDefaultState(), pos, matrixStack, immediate.getBuffer(RenderLayer.getEntitySolid(Blocks.CHEST.getDefaultState())), false, world.random, 42, 0);
//         ChestBlockEntityRenderer.drawShape(matrixStack, VertexConsumerProvider, box, 1.0f, 1.0f, 1.0f, 1.0f);
//     }
// }
    	
