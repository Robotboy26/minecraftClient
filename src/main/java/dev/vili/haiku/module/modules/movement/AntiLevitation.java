package dev.vili.haiku.module.modules.movement;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class AntiLevitation extends Module {
	public AntiLevitation() {
		super("AntiLevitation", "Disables levitation.", GLFW.GLFW_KEY_UNKNOWN, Category.MOVEMENT);
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		mc.options.useKey.setPressed(false);

		super.onDisable();
	}

    @HaikuSubscribe
    public void onUpdate(TickEvent event) {
        if (mc.player.hasStatusEffect(StatusEffects.LEVITATION)) {
            mc.player.setVelocity(mc.player.getVelocity().x, 0, mc.player.getVelocity().z);
        }
    }
}