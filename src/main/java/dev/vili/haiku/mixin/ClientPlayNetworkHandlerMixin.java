package dev.vili.haiku.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.vili.haiku.Haiku;
import dev.vili.haiku.utils.HaikuLogger;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onEntitySpawn", at = @At("HEAD"), cancellable = true)
    private void noweathereffects$cancelLightningSpawn(EntitySpawnS2CPacket packet, CallbackInfo ci) {
        if (Haiku.getInstance().getModuleManager().isModuleEnabled("NoWeather") && packet.getEntityType() == EntityType.LIGHTNING_BOLT) {
            HaikuLogger.info("Cancelled lightning spawn!");
            ci.cancel();
        }
    }

}
