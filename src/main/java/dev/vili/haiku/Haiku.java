/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.vili.haiku;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;

import javax.swing.JOptionPane;

import dev.vili.haiku.altmanager.AltManager;
import dev.vili.haiku.command.CommandManager;
import dev.vili.haiku.config.ConfigManager;
import dev.vili.haiku.eventbus.EventBus;
import dev.vili.haiku.module.ModuleManager;
import dev.vili.haiku.setting.SettingManager;
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
        Path currentPath = Paths.get("");
        String containingFolderName = currentPath.toAbsolutePath().getFileName().toString();
        HaikuLogger.info("Containing folder name: " + containingFolderName);
        if (containingFolderName.equals("run")) {
            modsFolder = "mods";
        } else if (containingFolderName.equals("minecraft")) {
            modsFolder = "mods";
        }

        Path folderPath = Paths.get("mods");
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-xr-x");
        try {
            Files.setPosixFilePermissions(folderPath, perms);
            HaikuLogger.info("Permissions set successfully for folder: " + folderPath);
        } catch (Exception e) {
            HaikuLogger.info("Error while setting permissions for folder: " + e.getMessage());
            e.printStackTrace();
        }

            // might not even need this because if I was better I could just use the gradle build include to do this too and it would be better but that fine
            try {
                getMod("graphutil-fabric-1.0.0-mc1.20.1.jar", "https://cdn.modrinth.com/data/cpBmCs66/versions/zmHteWGB/graphutil-fabric-1.0.0-mc1.20.1.jar", modsFolder);
                getMod("krypton-0.2.3.jar", "https://cdn.modrinth.com/data/fQEb0iXm/versions/jiDwS0W1/krypton-0.2.3.jar", modsFolder);
                getMod("dashloader-5.0.0-beta.2%2B1.20.0.jar", "https://cdn.modrinth.com/data/ZfQ3kTvR/versions/wgtrj8HS/dashloader-5.0.0-beta.2%2B1.20.0.jar", modsFolder);
                getMod("Fastload-Reforged-mc1.20.1-3.4.0.jar", "https://cdn.modrinth.com/data/kCpssoSb/versions/5caSj7kt/Fastload-Reforged-mc1.20.1-3.4.0.jar", modsFolder);
                getMod("c2me-fabric-mc1.20.1-0.2.0%2Balpha.11.0.jar", "https://cdn.modrinth.com/data/VSNURh3q/versions/T5Pkyhit/c2me-fabric-mc1.20.1-0.2.0%2Balpha.11.0.jar", modsFolder);
                getMod("iris-mc1.20.1-1.6.10.jar", "https://cdn.modrinth.com/data/YL57xq9U/versions/DsjYuGMO/iris-mc1.20.1-1.6.10.jar", modsFolder);
                getMod("lazydfu-0.1.3.jar", "https://cdn.modrinth.com/data/hvFnDODi/versions/0.1.3/lazydfu-0.1.3.jar", modsFolder);
                getMod("reeses_sodium_options-1.6.5%2Bmc1.20.1-build.95.jar", "https://cdn.modrinth.com/data/Bh37bMuy/versions/hCsMUZLa/reeses_sodium_options-1.6.5%2Bmc1.20.1-build.95.jar", modsFolder);
                getMod("sodium-extra-0.5.1%2Bmc1.20.1-build.112.jar", "https://cdn.modrinth.com/data/PtjYWJkn/versions/80a0J5Cn/sodium-extra-0.5.1%2Bmc1.20.1-build.112.jar", modsFolder);
                getMod("starlight-1.1.2%2Bfabric.dbc156f.jar", "https://cdn.modrinth.com/data/H8CaAYZC/versions/XGIsoVGT/starlight-1.1.2%2Bfabric.dbc156f.jar", modsFolder);
                getMod("indium-1.0.27%2Bmc1.20.1.jar", "https://cdn.modrinth.com/data/Orvt0mRa/versions/Lue6O9z9/indium-1.0.27%2Bmc1.20.1.jar", modsFolder);
                getMod("sodium-fabric-mc1.20.1-0.5.3.jar", "https://cdn.modrinth.com/data/AANobbMI/versions/4OZL6q9h/sodium-fabric-mc1.20.1-0.5.3.jar", modsFolder);
                getMod("modmenu-7.2.2.jar", "https://cdn.modrinth.com/data/mOgUt4GM/versions/lEkperf6/modmenu-7.2.2.jar", modsFolder);
                getMod("WorldTools-fabric-1.0.0.jar", "https://cdn.modrinth.com/data/FlFKBOIX/versions/SFaotVvV/WorldTools-fabric-1.0.0.jar", modsFolder);
                getMod("viafabricplus-2.8.7.jar", "https://cdn.modrinth.com/data/rIC2XJV4/versions/PdXzP7Px/viafabricplus-2.8.7.jar", modsFolder);
                getMod("fabric-api-0.90.7%2B1.20.1.jar", "https://cdn.modrinth.com/data/P7dR8mSH/versions/JXpzzvU6/fabric-api-0.90.7%2B1.20.1.jar", modsFolder);
            } finally {
                HaikuLogger.logger.info("Loaded mods!");
                HaikuLogger.logger.info("if this is your first time running haiku, please restart your game!");
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

    private void getMod(String modName, String modUrl, String modsFolder) {
        if (!new File(modsFolder, modName).exists()) {
            try {
                downloadMod(modUrl, modsFolder, modName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        Path modsFolder;
        if (System.getProperty("user.dir").endsWith("mods")) {
            modsFolder = Path.of("");
        } else {
            modsFolder = Path.of("mods");
        }
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
