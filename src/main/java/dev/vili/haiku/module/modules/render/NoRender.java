// /*
//  * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
//  * Copyright (c) 2021 Bleach and contributors.
//  *
//  * This source code is subject to the terms of the GNU General Public
//  * License, version 3. If a copy of the GPL was not distributed with this
//  * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
//  */
// package dev.vili.haiku.module.modules.render;

// import dev.vili.haiku.command.Command;
// import dev.vili.haiku.event.events.entity.EventBlockEntityRender;
// import dev.vili.haiku.event.events.entity.EventEntityRender;
// import dev.vili.haiku.event.events.EventParticle;
// import dev.vili.haiku.event.events.EventRenderOverlay;
// import dev.vili.haiku.event.events.EventRenderScreenBackground;
// import dev.vili.haiku.event.events.EventSoundPlay;
// import dev.vili.haiku.eventbus.HaikuSubscribe;
// //import dev.vili.haiku.gui.window.Window;
// import dev.vili.haiku.module.Module;
// import dev.vili.haiku.setting.settings.ModeSetting;
// import dev.vili.haiku.setting.settings.NumberSetting;
// import dev.vili.haiku.setting.settings.BooleanSetting;
// //import dev.vili.haiku.utils.io.BleachFileHelper;

// import org.lwjgl.glfw.GLFW;

// import com.google.gson.JsonElement;

// import net.minecraft.block.entity.SignBlockEntity;
// import net.minecraft.client.particle.CampfireSmokeParticle;
// import net.minecraft.client.particle.ElderGuardianAppearanceParticle;
// import net.minecraft.client.particle.ExplosionLargeParticle;
// import net.minecraft.client.particle.FireworksSparkParticle.FireworkParticle;
// import net.minecraft.entity.ExperienceOrbEntity;
// import net.minecraft.entity.FallingBlockEntity;
// import net.minecraft.entity.ItemEntity;
// import net.minecraft.entity.decoration.ArmorStandEntity;
// import net.minecraft.entity.projectile.thrown.SnowballEntity;
// import net.minecraft.entity.vehicle.AbstractMinecartEntity;
// import net.minecraft.particle.ParticleTypes;

// import net.minecraft.text.Text;

// public class NoRender extends Module {
// 	public final BooleanSetting overlay = new BooleanSetting("Overlays", "Removes certain overlays", true);
// 	public final BooleanSetting blindness = new BooleanSetting("Blindness", "Removes the blindness effect", true);
// 	public final BooleanSetting fire = new BooleanSetting("Fire", "Removes the fire overlay", true);
// 	public final BooleanSetting hurtcam = new BooleanSetting("Hurtcam", "Removes shaking when you get hurt", true);
// 	public final BooleanSetting liquid = new BooleanSetting("Liquid", "Removes the underwater overlay when you're in water", true);
// 	public final BooleanSetting pumpkin = new BooleanSetting("Pumpkin", "Removes the pumpkin overlay", true);
// 	public final BooleanSetting wobble = new BooleanSetting("Wobble", "Removes the nausea effect", true);
// 	public final BooleanSetting bossbar = new BooleanSetting("BossBar", "Removes bossbars", false);
// 	public final BooleanSetting gui = new BooleanSetting("Gui", "Makes the gui background more transparent", false);
// 	public final NumberSetting opacity = new NumberSetting("Opacity", "The opacity of the gui background", 0, 0, 1, 0.01);
// 	public final BooleanSetting frostbite = new BooleanSetting("Frostbite", "Removes the frostbite overlay when you walk in powdered snow", true);
// 	public final BooleanSetting world = new BooleanSetting("World", "Removes miscellaneous things in the world", true);
// 	public final BooleanSetting signs = new BooleanSetting("Signs", "Doesn't render signs", false);
// 	public final BooleanSetting totem = new BooleanSetting("Totem", "Removes the totem animation", false);
// 	public final BooleanSetting egCurse = new BooleanSetting("EG Curse", "Removes the elder guardian curse", true);
// 	public final BooleanSetting maps = new BooleanSetting("Maps", "Blocks mapart (useful if you're streaming)", false);
// 	public final BooleanSetting skylight = new BooleanSetting("Skylight", "Disables skylight updates to reduce skylight lag", false);
// 	public final BooleanSetting particles = new BooleanSetting("Particles", "Removes certain particles from the world", true);
// 	public final BooleanSetting campfires = new BooleanSetting("Campfires", "Removes campfire smoke particles", true);
// 	public final BooleanSetting explosions = new BooleanSetting("Explosions", "Removes explosion particles", false);
// 	public final NumberSetting explosionKeep = new NumberSetting("Keep", "How much of the explosion particles to keep", 0, 0, 100, 1);
// 	public final BooleanSetting fireworks = new BooleanSetting("Fireworks", "Removes firework explosion particles", false);
// 	public final BooleanSetting entities = new BooleanSetting("Entities", "Removes certain entities from the world", true);
// 	public final BooleanSetting armorStands = new BooleanSetting("Armor Stands", "Removes armor stands", false);
// 	public final BooleanSetting fallingBlocks = new BooleanSetting("Falling Blocks", "Removes falling blocks", false);
// 	public final BooleanSetting minecarts = new BooleanSetting("Minecarts", "Removes minecarts", false);
// 	public final BooleanSetting snowballs = new BooleanSetting("Snowballs", "Removes snowballs", false);
// 	public final BooleanSetting xpOrbs = new BooleanSetting("Xp Orbs", "Removes experience orbs", false);
// 	public final BooleanSetting items = new BooleanSetting("Items", "Removes items", false);


// 	public Text[] signText = new Text[] { Text.empty(), Text.empty(), Text.empty(), Text.empty() };

// 	public NoRender() {
// 		super("NoRender", "Blocks certain elements from rendering.", GLFW.GLFW_KEY_UNKNOWN, Category.RENDER);

// 		JsonElement signText = null;

// 		if (signText != null) {
// 			for (int i = 0; i < Math.min(4, signText.getAsJsonArray().size()); i++) {
// 				this.signText[i] = Text.literal(signText.getAsJsonArray().get(i).getAsString());
// 			}
// 		}
// 	}

// 	public BooleanSetting getOverlayChild(int overlayChild) {
// 		return 
// 	}
// 	List<Integer> myList = new ArrayList<Integer>();
// 	myList.add(10);
// 	myList.add(20);
// 	myList.add(30);

// 	public int getSecondElement() {
// 		return myList.get(1);
// 	}
	
// 	public SettingToggle getWorldChild(int worldChild) {
// 		return getSetting(1).asToggle().getChild(worldChild).asToggle();
// 	}
	
// 	public SettingToggle getParticleChild(int particleChild) {
// 		return getSetting(2).asToggle().getChild(particleChild).asToggle();
// 	}
	
// 	public SettingToggle getEntityChild(int entityChild) {
// 		return getSetting(3).asToggle().getChild(entityChild).asToggle();
// 	}

// 	public boolean isOverlayToggled(int overlayChild) {
// 		return isEnabled() && getSetting(0).asToggle().getState() && getOverlayChild(overlayChild).getState();
// 	}

// 	public boolean isWorldToggled(int worldChild) {
// 		return isEnabled() && getSetting(1).asToggle().getState() && getWorldChild(worldChild).getState();
// 	}

// 	public boolean isParticleToggled(int particleChild) {
// 		return isEnabled() && getSetting(2).asToggle().getState() && getParticleChild(particleChild).getState();
// 	}

// 	public boolean isEntityToggled(int entityChild) {
// 		return isEnabled() && getSetting(3).asToggle().getState() && getEntityChild(entityChild).getState();
// 	}

// 	@HaikuSubscribe
// 	public void onRenderOverlay(EventRenderOverlay event) {
// 		if (event.getTexture().getPath().equals("textures/misc/pumpkinblur.png") && isOverlayToggled(4)) {
// 			event.setCancelled(true);
// 		} else if (event.getTexture().getPath().equals("textures/misc/powder_snow_outline.png") && isOverlayToggled(8)) {
// 			event.setCancelled(true);
// 		}
// 	}

// 	@HaikuSubscribe
// 	public void onEntityRender(EventEntityRender.Single.Pre event) {
// 		if ((isEntityToggled(0) && event.getEntity() instanceof ArmorStandEntity)
// 				|| (isEntityToggled(1) && event.getEntity() instanceof FallingBlockEntity)
// 				|| (isEntityToggled(2) && event.getEntity() instanceof AbstractMinecartEntity)
// 				|| (isEntityToggled(3) && event.getEntity() instanceof SnowballEntity)
// 				|| (isEntityToggled(4) && event.getEntity() instanceof ExperienceOrbEntity)
// 				|| (isEntityToggled(5) && event.getEntity() instanceof ItemEntity)) {
// 			event.setCancelled(true);
// 		}
// 	}

// 	@HaikuSubscribe
// 	public void onSignRender(EventBlockEntityRender.Single.Pre event) {
// 		if (event.getBlockEntity() instanceof SignBlockEntity && isWorldToggled(0)) {
// 			SettingToggle signSetting = getWorldChild(0);

// 			if (signSetting.getChild(0).asMode().getMode() == 0) {
// 				event.setCancelled(true);
// 			} else {
// 				SignBlockEntity sign = new SignBlockEntity(event.getBlockEntity().getPos(), event.getBlockEntity().getCachedState());
// 				sign.setWorld(mc.world);

// 				if (signSetting.getChild(0).asMode().getMode() == 2) {
// 					for (int i = 0; i < 4; i++) {
// 						sign.setTextOnRow(i, signText[i]);
// 					}
// 				}

// 				event.setBlockEntity(sign);
// 			}
// 		}
// 	}

// 	@HaikuSubscribe
// 	public void onParticle(EventParticle.Normal event) {
// 		if ((isWorldToggled(2) && event.getParticle() instanceof ElderGuardianAppearanceParticle)
// 				|| (isParticleToggled(0) && event.getParticle() instanceof CampfireSmokeParticle)
// 				|| (isParticleToggled(1) && event.getParticle() instanceof ExplosionLargeParticle && Math.abs(event.getParticle().getBoundingBox().hashCode()) % 101 >= getParticleChild(1).getChild(0).asSlider().getValueInt())
// 				|| (isParticleToggled(2) && event.getParticle() instanceof FireworkParticle)) {
// 			event.setCancelled(true);
// 		}
// 	}

// 	@HaikuSubscribe
// 	public void onParticleEmitter(EventParticle.Emitter event) {
// 		if (isWorldToggled(1) && getWorldChild(1).getChild(0).asToggle().getState() && event.getEffect().getType() == ParticleTypes.TOTEM_OF_UNDYING) {
// 			event.setCancelled(true);
// 		}
// 	}

// 	@HaikuSubscribe
// 	public void onSoundPlay(EventSoundPlay.Normal event) {
// 		String path = event.getInstance().getId().getPath();
// 		if (isWorldToggled(1) && getWorldChild(1).getChild(1).asToggle().getState() && path.equals("item.totem.use")) {
// 			event.setCancelled(true);
// 		} else if (isWorldToggled(2) && path.equals("entity.elder_guardian.curse")) {
// 			event.setCancelled(true);
// 		}
// 	}

// 	@HaikuSubscribe
// 	public void onRenderGuiBackground(EventRenderScreenBackground event) {
// 		if (mc.world != null && isOverlayToggled(7)) {
// 			SettingToggle guiSetting = getOverlayChild(7);
// 			int opacity = (int) (guiSetting.getChild(0).asSlider().getValue() * 255);

// 			if (opacity != 0) {
// 				int opacity2 = (int) (opacity * 0.93);
// 				Window.verticalGradient(
// 						event.getMatrices(),
// 						0, 0,
// 						mc.currentScreen.width, mc.currentScreen.height,
// 						(opacity2 << 24) | 0x101010,
// 						(opacity  << 24) | 0x101010);
// 			}

// 			event.setCancelled(true);
// 		}
// 	}
// }
