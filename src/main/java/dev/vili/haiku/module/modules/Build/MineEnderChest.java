package dev.vili.haiku.module.modules.Build;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.EventBlockBreakCooldown;
import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.ModeSetting;
import dev.vili.haiku.setting.settings.NumberSetting;
import dev.vili.haiku.utils.HaikuLogger;
import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Key;
import net.minecraft.util.ActionResult;
import net.minecraft.client.util.InputUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.utils.Utils;

public class MineEnderChest extends Module {
    public static boolean shouldRightClick = false;
    public static int clickTickInterval = 1;
    private int rightCooldown = 0;

    public MineEnderChest() {
        super("MineEnderChest", "Auto mines ender chests.", GLFW.GLFW_KEY_UNKNOWN, Category.MISC);
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

    @Override
    public void onEnable() {

        super.onEnable();
    }

@HaikuSubscribe
public void onTick(TickEvent event) {
    mc.player.setSneaking(true);
    BlockHitResult result = (BlockHitResult) mc.player.raycast(5.0D, 0.0F, false);
    if (result.getType() == HitResult.Type.BLOCK) {
        BlockPos blockPos = result.getBlockPos();
        BlockState blockState = mc.world.getBlockState(blockPos);
        if (blockState.getBlock().getName().getString().contains("Ender Chest")) {
            if (blockState.getHardness(mc.world, blockPos) != -1) {
                    mc.interactionManager.attackBlock(blockPos, Direction.UP);
                    mc.player.swingHand(Hand.MAIN_HAND);
                    mc.interactionManager.cancelBlockBreaking();
                    while (blockState.getHardness(mc.world, blockPos) > 0) {
                        mc.interactionManager.updateBlockBreakingProgress(blockPos, Direction.UP);
                        mc.player.swingHand(Hand.MAIN_HAND);
                        blockState = mc.world.getBlockState(blockPos);
                }
            }
            } else {
                HaikuLogger.info("Ender Chest needs placeing!");
                rightCooldown++;
                if (rightCooldown > 5) {
                    Utils.rightClick();
                    rightCooldown = 0;
                }
            }
        }
}

public void mcReflRightClick() {
    mcReflInvokeMethod(getRightClickMouseMethodMapping());
}

private void mcReflInvokeMethod(String name) {
    mcReflInvokeMethod(name, new Class[0], new Object[0]);
}

private void mcReflInvokeMethod(String name, Class<?>[] types, Object[] args) {
    try {
        HaikuLogger.info("Invoking " + name);
        Method method = MinecraftClient.class.getDeclaredMethod(name, types);
        method.setAccessible(true); // make the method accessible
        method.invoke(MinecraftClient.getInstance(), args);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
    }
}

public String getRightClickMouseMethodMapping() {
    return "rightClickMouse"; // func_147121_ag
}

}