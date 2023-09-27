package dev.vili.haiku.module.modules.render;

import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;
import net.minecraft.entity.effect.StatusEffects;

import org.lwjgl.glfw.GLFW;

public final class TrueSight extends Module 
{
    public final BooleanSetting fire = new BooleanSetting("Fire", "If selected you will not see when you are on fire", true);
    public final BooleanSetting blindness = new BooleanSetting("Blindness", "If selected you will not see when you are blinded", true);
    public final BooleanSetting darkness = new BooleanSetting("Darkness", "If selected you will not see when you are in darkness", true);
    public final BooleanSetting nausea = new BooleanSetting("Nausea", "If selected you will not see when you are nauseated", true);
    public final BooleanSetting wither = new BooleanSetting("Wither", "If selected you will not see when you are withered", true); 
    public final BooleanSetting allPotion = new BooleanSetting("AllPotions", "This will disable all isual effect for every potion", false);   
    public TrueSight() {
        super("TrueSight", "Removes many rendering effects.", GLFW.GLFW_KEY_UNKNOWN, Category.RENDER);
        this.addSettings(fire, blindness, darkness, nausea, wither, allPotion);
    }

    @Override
    public void onEnable() {
        if (fire.isEnabled()) mc.player.setOnFire(false);
        if (blindness.isEnabled()) mc.player.removeStatusEffect(StatusEffects.BLINDNESS);
        if (darkness.isEnabled()) mc.player.removeStatusEffect(StatusEffects.DARKNESS);
        if (nausea.isEnabled()) mc.player.removeStatusEffect(StatusEffects.NAUSEA);
        if (wither.isEnabled()) mc.player.removeStatusEffect(StatusEffects.WITHER);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @HaikuSubscribe
    public void onTick() {
        if (fire.isEnabled() && mc.player.isOnFire()) mc.player.setOnFire(false);
        if (blindness.isEnabled() && mc.player.hasStatusEffect(StatusEffects.BLINDNESS)) mc.player.removeStatusEffect(StatusEffects.BLINDNESS);
        if (darkness.isEnabled() && mc.player.hasStatusEffect(StatusEffects.DARKNESS)) mc.player.removeStatusEffect(StatusEffects.DARKNESS);
        if (nausea.isEnabled() && mc.player.hasStatusEffect(StatusEffects.NAUSEA)) mc.player.removeStatusEffect(StatusEffects.NAUSEA);
        if (wither.isEnabled() && mc.player.hasStatusEffect(StatusEffects.WITHER)) mc.player.removeStatusEffect(StatusEffects.WITHER);
    }
}