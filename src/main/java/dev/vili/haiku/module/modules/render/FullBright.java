package dev.vili.haiku.module.modules.render;

import dev.vili.haiku.MixinResources.mixinterface.ISimpleOption;
import dev.vili.haiku.event.events.TickEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.ModeSetting;
import dev.vili.haiku.utils.HaikuLogger;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.tick.Tick;

import org.lwjgl.glfw.GLFW;

public class FullBright extends Module {
    //public final ModeSetting mode = new ModeSetting("Mode", Mode.Gamma);

    public FullBright() {
        super("FullBright", "FullBright", GLFW.GLFW_KEY_C, Module.Category.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

//     @HaikuSubscribe
//     public void onTick(TickEvent event) {
//         if (Mode.getMode() == "Lightning") {
//             mc.world.setLightningTicksLeft(10);
//         }
//         if (Mode.getMode() == "Night Vision") {
//             mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 2147483647));
//         }
//     }
// }