package dev.vili.haiku.module.modules.render;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.mixinterface.ISimpleOption;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.ModeSetting;
import dev.vili.haiku.utils.HaikuLogger;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.tick.Tick;

import org.lwjgl.glfw.GLFW;

public final class FullBright extends Module 
{
    private double previousValue = 0.0;
    public final ModeSetting Mode = new ModeSetting("Mode", "The mode of FullBright.", "Lightning", "Night Vision", "Lightning");
    public FullBright() {
        super("FullBright", "See in the dark.", GLFW.GLFW_KEY_UNKNOWN, Category.RENDER);
        this.addSettings(Mode);
    }

    @Override
	public void onDisable() {
		@SuppressWarnings("unchecked")
		ISimpleOption<Double> gamma =
				(ISimpleOption<Double>)(Object)mc.options.getGamma();
		gamma.forceSetValue(previousValue);
	}

	@Override
	public void onEnable() {
		this.previousValue = mc.options.getGamma().getValue();
		@SuppressWarnings("unchecked")
		ISimpleOption<Double> gamma =
				(ISimpleOption<Double>)(Object)mc.options.getGamma();
		gamma.forceSetValue(10000.0);
	}
}