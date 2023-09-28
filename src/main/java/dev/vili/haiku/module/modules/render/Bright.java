package dev.vili.haiku.module.modules.render;

import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import org.lwjgl.glfw.GLFW;

public final class Bright extends Module 
{
    public Bright() {
        super("Bright", "Gives you night vision.", GLFW.GLFW_KEY_C, Category.RENDER);
    }
    @Override
    public void onEnable() {
        mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 2147483647));
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        super.onDisable();
    }

    @HaikuSubscribe
    public void onTick() {
        if (!mc.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 2147483647));
        }
    }
}