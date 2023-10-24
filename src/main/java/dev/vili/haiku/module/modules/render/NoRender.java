/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.module.modules.render;

import dev.vili.haiku.event.events.world.ChunkOcclusionEvent;
import dev.vili.haiku.event.events.world.ParticleEvent;
import dev.vili.haiku.setting.settings.*;
import dev.vili.haiku.module.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;

import java.util.List;
import java.util.Set;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.setting.settings.ModeSetting;

public class NoRender extends Module
{
    public final BooleanSetting noPortalOverlay = new BooleanSetting("PortalOverlay-NoRener", "Disables rendering of the nether portal overlay.", false);
    public final BooleanSetting noSpyglassOverlay = new BooleanSetting("SpyglassOverlay-NoRender", "Disables rendering of the spyglass overlay.", false);
    public final BooleanSetting noNausea = new BooleanSetting("Nausea-NoRender", "Disables rendering of nausea.", false);
    public final BooleanSetting noPumpkinOverlay = new BooleanSetting("PumpkinOverlay-NoRender", "Disables rendering of the pumpkin head overlay", false);
    public final BooleanSetting noPowderedSnowOverlay = new BooleanSetting("PowderedSnowOverlay-NoRender", "Disables rendering of the powdered snow overlay.", false);
    public final BooleanSetting noFireOverlay = new BooleanSetting("FireOverlay-NoRender", "Disables rendering of the fire overlay.", false);
    public final BooleanSetting noLiquidOverlay = new BooleanSetting("LiquidOverlay-NoRender", "Disables rendering of the liquid overlay.", false);
    public final BooleanSetting noInWallOverlay = new BooleanSetting("InWallOverlay-NoRender", "Disables rendering of the overlay when inside blocks.", false);
    public final BooleanSetting noVignette = new BooleanSetting("Vignette-NoRender", "Disables rendering of the vignette overlay.", false);
    public final BooleanSetting noGuiBackground = new BooleanSetting("GuiBackground-NoRender", "Disables rendering of the GUI background overlay.", false);
    public final BooleanSetting noTotemAnimation = new BooleanSetting("TotemAnimation-NoRender", "Disables rendering of the totem animation when you pop a totem.", false);
    public final BooleanSetting noEatParticles = new BooleanSetting("EatingParticles-NoRender", "Disables rendering of eating particles.", false);
    public final BooleanSetting noEnchantGlint = new BooleanSetting("EnchantmentGlint-NoRender", "Disables rending of the enchantment glint.", false);
    public final BooleanSetting noBossBar = new BooleanSetting("BossBar-NoRender", "Disables rendering of boss bars.", false);
    public final BooleanSetting noScoreboard = new BooleanSetting("Scoreboard-NoRender", "Disables rendering of the scoreboard.", false);
    public final BooleanSetting noCrosshair = new BooleanSetting("Crosshair-NoRender", "Disables rendering of the crosshair.", false);
    public final BooleanSetting noHeldItemName = new BooleanSetting("HeldItemName-NoRender", "Disables rendering of the held item name.", false);
    public final BooleanSetting noObfuscation = new BooleanSetting("Obfuscation-NoRender", "Disables obfuscation styling of characters.", false);
    public final BooleanSetting noPotionIcons = new BooleanSetting("PotionIcons-NoRender", "Disables rendering of status effect icons.", false);
    public final BooleanSetting noMessageSignatureIndicator = new BooleanSetting("MessageSignatureIndicator-NoRender", "Disables chat message signature indicator on the left of the message.", false);
    public final BooleanSetting noWeather = new BooleanSetting("Weather-NoRender", "Disables rendering of weather.", false);
    public final BooleanSetting noBlindness = new BooleanSetting("Blindness-NoRender", "Disables rendering of blindness.", false);
    public final BooleanSetting noDarkness = new BooleanSetting("Darkness-NoRender", "Disables rendering of darkness.", false);
    public final BooleanSetting noFog = new BooleanSetting("Fog-NoRender", "Disables rendering of fog.", false);
    public final BooleanSetting noEnchTableBook = new BooleanSetting("EnchantmentTableBook-NoRender", "Disables rendering of books above enchanting tables.", false);
    public final BooleanSetting noSignText = new BooleanSetting("SignText-NoRender", "Disables rendering of text on signs.", false);
    public final BooleanSetting noBlockBreakParticles = new BooleanSetting("BlockBreakParticles-NoRender", "Disables rendering of block-break particles.", false);
    public final BooleanSetting noBlockBreakOverlay = new BooleanSetting("BlockBreakOverlay-NoRender", "Disables rendering of block-break overlay.", false);
    public final BooleanSetting noSkylightUpdates = new BooleanSetting("SkylightUpdates-NoRender", "Disables rendering of skylight updates.", false);
    public final BooleanSetting noBeaconBeams = new BooleanSetting("BeaconBeams-NoRender", "Disables rendering of beacon beams.", false);
    public final BooleanSetting noFallingBlocks = new BooleanSetting("FallingBlocks-NoRender", "Disables rendering of falling blocks.", false);
    public final BooleanSetting noCaveCulling = new BooleanSetting("CaveCulling-NoRender", "Disables Minecraft's cave culling algorithm.", false);
    public final BooleanSetting noMapMarkers = new BooleanSetting("MapMarkers-NoRender", "Disables markers on maps.", false);
    public final ModeSetting bannerRender = new ModeSetting("Banners-NoRender", "Changes rendering of banners.", "Everything", "NoRender", "NoRenderAndNoEffects");
    public final BooleanSetting noFireworkExplosions = new BooleanSetting("FireworkExplosions-NoRender", "Disables rendering of firework explosions.", false);
    public final ModeSetting particles = new ModeSetting("Particles-NoRender", "Particles to not render.", "All", "Ambient_Entity_Effect", "Block_Dust", "Bubble", "Cloud", "Crit", "Current_Down", "Damage_Indicator", "Dragon_Breath", "Dripping_Lava", "Dripping_Water", "Dust", "Effect", "Elder_Guardian", "Enchanted_Hit", "Enchant", "End_Rod", "Entity_Effect", "Explosion", "Explosion_Empty", "Explosion_Huge", "Explosion_Large", "Explosion_Normal", "Falling_Dust", "Firework", "Fishing", "Flame", "Flash", "Footstep", "Happy_Villager", "Heart", "Instant_Effect", "Item", "Item_Slab", "Item_Snowball", "Large_Smoke", "Lava", "Mycelium", "Note", "Poof", "Portal", "Rain", "Smoke", "Spit", "Squid_Ink", "Sweep_Attack", "Totem_Of_Undying", "Underwater", "Vibration", "Wax_Off", "Wax_On", "White_Ash", "Witch", "Wither", "Wither_Boss", "Wither_Skull");
    public final BooleanSetting noBarrierInvis = new BooleanSetting("BarrierInvisibility-NoRender", "Disables barriers being invisible when not holding one.", false);
    public final BooleanSetting noTextureRotations = new BooleanSetting("TextureRotations-NoRender", "Changes texture rotations and model offsets to use a random value instead of the block position.", false);
    public final BooleanSetting noEnchantmentTableBook = new BooleanSetting("EnchantmentTableBook-NoRender", "Disables rendering of books above enchanting tables.", false);
    public final ModeSetting entities = new ModeSetting("Entities-NoRender", "Disables rendering of selected entities.", "None", "Item", "XPOrb", "AreaEffectCloud", "Egg", "LeashKnot", "Painting", "Arrow", "Snowball", "Fireball", "SmallFireball", "EnderPearl", "EyeOfEnderSignal", "Potion", "ExpBottle", "FireworkRocketEntity", "TippedArrow", "SpectralArrow", "ShulkerBullet", "DragonFireball", "LlamaSpit", "EvokerFangs", "FishingBobber", "Trident", "WitherSkull", "Boat", "Minecart", "MinecartChest", "MinecartCommandBlock", "MinecartFurnace", "MinecartHopper", "MinecartSpawner", "MinecartTNT", "MinecartEmpty", "ArmorStand", "ChestMinecart", "CommandBlockMinecart", "FurnaceMinecart", "HopperMinecart", "SpawnerMinecart", "TNTMinecart", "ItemFrame", "Wither", "EnderCrystal", "SplashPotion", "Llama", "Villager", "Endermite", "Guardian", "Rabbit", "EnderDragon", "WitherSkeleton", "Stray", "Egg", "Ghast", "Blaze", "ZombiePigman", "Husk", "ZombieVillager", "SkeletonHorse", "ZombieHorse", "Donkey", "Mule", "Bat", "Pig", "Sheep", "Cow", "Chicken", "Squid", "Wolf", "Mooshroom", "SnowMan", "Ozelot", "VillagerGolem", "EntityHorse", "Rabbit", "PolarBear", "Llama", "Parrot", "Vex", "Evoker", "Vindicator", "Illusioner", "Creeper", "Skeleton", "Spider", "Giant", "Zombie", "Slime", "Ghast", "PigZombie", "Enderman", "CaveSpider", "Silverfish", "Blaze", "MagmaCube", "EnderDragon", "WitherBoss", "Bat", "Witch", "Endermite", "Guardian", "Shulker", "Pig", "Sheep", "Cow", "Chicken", "Squid", "Wolf", "Mooshroom", "SnowMan", "Ozelot", "IronGolem", "Horse", "Rabbit", "PolarBear", "Llama", "Parrot", "Villager", "EnderCrystal", "Turtle", "Phantom", "Trident", "Cod", "Salmon", "Pufferfish", "TropicalFish", "Drowned", "Dolphin", "Cat", "Panda", "Pillager", "Ravager", "TraderLlama", "WanderingTrader", "Fox", "Bee", "Hoglin", "Piglin", "Strider", "Zoglin", "PiglinBrute");
    public final BooleanSetting dropSpawnPacket = new BooleanSetting("drop-spawn-packets-NoRender", "WARNING! Drops all spawn packets of entities selected in the above list.", false);
    public final BooleanSetting noArmor = new BooleanSetting("Armor-NoRender", "Disables rendering of armor on entities.", false);
    public final BooleanSetting noInvisibility = new BooleanSetting("Invisibility-NoRender", "Shows invisible entities.", false);
    public final BooleanSetting noGlowing = new BooleanSetting("glowing", "Disables rendering of the glowing effect", false);
    public final BooleanSetting noMobInSpawner = new BooleanSetting("spawner-entities", "Disables rendering of spinning mobs inside of mob spawners", false);
    public final BooleanSetting noDeadEntities = new BooleanSetting("dead-entities", "Disables rendering of dead entities", false);
    public final BooleanSetting noEntityRender = new BooleanSetting("entity-render", "Disables rendering of entities", false);
    public final BooleanSetting noNametags = new BooleanSetting("nametags", "Disables rendering of entity nametags", false);
    
    public NoRender() {
        super("NoRender", "Disables certain animations or overlays from rendering.", GLFW.GLFW_KEY_N, Category.RENDER);
        this.addSettings(noPortalOverlay, noSpyglassOverlay, noNausea, noPumpkinOverlay, noPowderedSnowOverlay, noFireOverlay, noLiquidOverlay, noInWallOverlay, noVignette, noGuiBackground, noTotemAnimation, noEatParticles, noEnchantGlint, noBossBar, noScoreboard, noCrosshair, noHeldItemName, noObfuscation, noPotionIcons, noMessageSignatureIndicator, noWeather, noBlindness, noDarkness, noFog, noEnchTableBook, noSignText, noBlockBreakParticles, noBlockBreakOverlay, noSkylightUpdates, noBeaconBeams, noFallingBlocks, noCaveCulling, noMapMarkers, bannerRender, noFireworkExplosions, particles, noBarrierInvis, noTextureRotations, noEnchantmentTableBook, noEntityRender, entities, dropSpawnPacket, noArmor, noInvisibility, noGlowing, noMobInSpawner, noDeadEntities, noNametags);
    }

    @Override
    public void onDisable() {
        if (noCaveCulling.isEnabled()) mc.worldRenderer.reload();
    }

    // Overlay

    public boolean noPortalOverlay() {
        return noPortalOverlay.isEnabled();
    }

    public boolean noSpyglassOverlay() {
        return noSpyglassOverlay.isEnabled();
    }

    public boolean noNausea() {
        return noNausea.isEnabled();
    }

    public boolean noPumpkinOverlay() {
        return noPumpkinOverlay.isEnabled();
    }

    public boolean noFireOverlay() {
        return noFireOverlay.isEnabled();
    }

    public boolean noLiquidOverlay() {
        return noLiquidOverlay.isEnabled();
    }

    public boolean noPowderedSnowOverlay() {
        return noPowderedSnowOverlay.isEnabled();
    }

    public boolean noInWallOverlay() {
        return noInWallOverlay.isEnabled();
    }

    public boolean noVignette() {
        return noVignette.isEnabled();
    }

    public boolean noGuiBackground() {
        return noGuiBackground.isEnabled();
    }

    public boolean noTotemAnimation() {
        return noTotemAnimation.isEnabled();
    }

    public boolean noEatParticles() {
        return noEatParticles.isEnabled();
    }

    public boolean noEnchantGlint() {
        return noEnchantGlint.isEnabled();
    }

    // HUD

    public boolean noBossBar() {
        return noBossBar.isEnabled();
    }

    public boolean noScoreboard() {
        return noScoreboard.isEnabled();
    }

    public boolean noCrosshair() {
        return noCrosshair.isEnabled();
    }

    public boolean noHeldItemName() {
        return noHeldItemName.isEnabled();
    }

    public boolean noObfuscation() {
        return noObfuscation.isEnabled();
    }

    public boolean noPotionIcons() {
        return noPotionIcons.isEnabled();
    }

    public boolean noMessageSignatureIndicator() {
        return noMessageSignatureIndicator.isEnabled();
    }

    // World

    public boolean noWeather() {
        return noWeather.isEnabled();
    }

    public boolean noBlindness() {
        return noBlindness.isEnabled();
    }

    public boolean noDarkness() {
        return noDarkness.isEnabled();
    }

    public boolean noFog() {
        return noFog.isEnabled();
    }

    public boolean noEnchTableBook() {
        return noEnchTableBook.isEnabled();
    }

    public boolean noSignText() {
        return noSignText.isEnabled();
    }

    public boolean noBlockBreakParticles() {
        return noBlockBreakParticles.isEnabled();
    }

    public boolean noBlockBreakOverlay() {
        return noBlockBreakOverlay.isEnabled();
    }

    public boolean noSkylightUpdates() {
        return noSkylightUpdates.isEnabled();
    }

    public boolean noBeaconBeams() {
        return noBeaconBeams.isEnabled();
    }

    public boolean noFallingBlocks() {
        return noFallingBlocks.isEnabled();
    }

    @EventHandler
    private void onChunkOcclusion(ChunkOcclusionEvent event) {
        if (noCaveCulling.isEnabled()) event.cancel();
    }

    public boolean noMapMarkers() {
        return noMapMarkers.isEnabled();
    }

    // public BannerRenderMode getBannerRenderMode() {
    //     if (!isActive()) return BannerRenderMode.Everything;
    //     else return bannerRender.isEnabled();
    // }

    public boolean noFireworkExplosions() {
        return noFireworkExplosions.isEnabled();
    }

    @EventHandler
    private void onAddParticle(ParticleEvent event) {
        if (noWeather.isEnabled() && event.particle.getType() == ParticleTypes.RAIN) {
            event.cancel();
        } else if (noFireworkExplosions.isEnabled() && event.particle.getType() == ParticleTypes.FIREWORK) {
            event.cancel();
        } else if (particles.getMode().equals(event.particle.getType())) {
            event.cancel();
        }
    }

    public boolean noBarrierInvis() {
        return noBarrierInvis.isEnabled();
    }

    public boolean noTextureRotations() {
        return noTextureRotations.isEnabled();
    }

    // Entity

    public boolean noEntity(Entity entity) {
        return entities.getMode().equals(entity.getType().getName());
    }

    public boolean noEntity(EntityType<?> entity) {
        return entities.getMode().equals(entity.getName());
    }

    public boolean getDropSpawnPacket() {
        return dropSpawnPacket.isEnabled();
    }

    public boolean noArmor() {
        return noArmor.isEnabled();
    }

    public boolean noInvisibility() {
        return noInvisibility.isEnabled();
    }

    public boolean noGlowing() {
        return noGlowing.isEnabled();
    }

    public boolean noMobInSpawner() {
        return noMobInSpawner.isEnabled();
    }

    public boolean noDeadEntities() {
        return noDeadEntities.isEnabled();
    }

    public boolean noNametags() {
        return noNametags.isEnabled();
    }

    public enum BannerRenderMode {
        Everything,
        Pillar,
        None
    }
}
