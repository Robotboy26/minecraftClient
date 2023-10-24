/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.vili.haiku;

import dev.vili.haiku.command.CommandManager;
import dev.vili.haiku.config.ConfigManager;
import dev.vili.haiku.eventbus.EventBus;
import dev.vili.haiku.module.ModuleManager;
import dev.vili.haiku.setting.SettingManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.ServiceLoader;

import dev.vili.haiku.altmanager.AltManager;
import dev.vili.haiku.utils.HaikuLogger;
import meteordevelopment.orbit.IEventBus;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;

/**
 * Main class for haiku.
 */
public class Haiku implements ModInitializer {
    public static final String MOD_NAME = "Haiku";
    public static final String MOD_VERSION = "1.0";
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    private static Haiku INSTANCE;
    private final EventBus EVENT_BUS = new EventBus();
    public static final IEventBus OEVENT_BUS = new meteordevelopment.orbit.EventBus();
    private final ModuleManager MODULE_MANAGER = new ModuleManager();
    private final CommandManager COMMAND_MANAGER = new CommandManager();
    private final SettingManager SETTING_MANAGER = new SettingManager();
    private final ConfigManager CONFIG_MANAGER = new ConfigManager();
    public final AltManager altManager = new AltManager();
    public boolean NoWeather = false;

    public Haiku() {
        INSTANCE = this;
    }

    /**
     * Gets the instance of Haiku.
     */
    public static Haiku getInstance() {
        return INSTANCE;
    }

    /**
     * Called when haiku is initialized.
     */
    @Override
    public void onInitialize() {
        // Load mods
        String modsFolder = "mods";
        String modName = "";

            try {
                modName = "WorldTools-fabric-1.0.0.jar";
                downloadMod("https://cdn.modrinth.com/data/FlFKBOIX/versions/SFaotVvV/WorldTools-fabric-1.0.0.jar", modsFolder, modName);
                modName = "fabric-language-kotlin-1.10.10+kotlin.1.9.10.jar";
                downloadMod("https://cdn.modrinth.com/data/Ha28R6CL/versions/48ri5y9r/fabric-language-kotlin-1.10.10%2Bkotlin.1.9.10.jar", modsFolder, modName);
            } catch (IOException e) {
                e.printStackTrace();
            }

        HaikuLogger.logger.info(MOD_NAME + " v" + MOD_VERSION + " (phase 1) has initialized!");
        CONFIG_MANAGER.load();
        HaikuLogger.logger.info("Loaded config!");

        // Save configs on shutdown
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            CONFIG_MANAGER.save();
            altManager.saveAlts();
            HaikuLogger.logger.info("Saved config!");
        });

        OEVENT_BUS.subscribe(this);
    }

    public static void downloadMod(String url, String modsFolder, String modName) throws IOException {
    String destPath = String.format("%s/%s", modsFolder, modName);
    File dest = new File(destPath);
    URL modUrl = new URL(url);
    URLConnection conn = modUrl.openConnection();
    try (InputStream in = conn.getInputStream();
         FileOutputStream out = new FileOutputStream(dest)) {

        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
            }   
        }
    HaikuLogger.logger.info("Downloaded mod!" + modName);
    }
    
    public void loadMod() {
        Path modsFolder = Path.of("mods");
        Path modFilePath = modsFolder.resolve("mod.jar");

        if (modFilePath.toFile().exists()) {
            try {
                URLClassLoader classLoader = new URLClassLoader(new URL[] { modFilePath.toUri().toURL() }, getClass().getClassLoader());

                ServiceLoader<ModInitializer> initializerLoader = ServiceLoader.load(ModInitializer.class, classLoader);
                Iterator<ModInitializer> iterator = initializerLoader.iterator();
                if (iterator.hasNext()) {
                    ModInitializer modInitializer = iterator.next();
                    modInitializer.onInitialize();
                } else {
                    throw new RuntimeException("Failed to find mod initializer");
                }

                classLoader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the event bus.
     */
    public EventBus getEventBus() {
        return EVENT_BUS;
    }

    /**
     * Gets the module manager.
     */
    public ModuleManager getModuleManager() {
        return MODULE_MANAGER;
    }

    /**
     * Gets the command manager.
     */
    public CommandManager getCommandManager() {
        return COMMAND_MANAGER;
    }

    /**
     * Gets the setting manager.
     */
    public SettingManager getSettingManager() {
        return SETTING_MANAGER;
    }

    /**
     * Gets the config manager.
     */
    public ConfigManager getConfigManager() {
        return CONFIG_MANAGER;
    }
}
