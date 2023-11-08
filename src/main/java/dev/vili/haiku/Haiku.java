/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.vili.haiku;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
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

import dev.vili.haiku.MixinResources.altmanager.AltManager;
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
        // loading mods "settings"
        String modfolder = "mods";

        // Load mods
        String modsFolder = modfolder;
        Path currentPath = Paths.get("");
        String containingFolderName = currentPath.toAbsolutePath().getFileName().toString();
        HaikuLogger.info("Containing folder name: " + containingFolderName);
        if (containingFolderName.equals("run")) {
            modsFolder = modfolder;
        } else if (containingFolderName.equals("minecraft")) {
            modsFolder = modfolder;
        }

        if (!new File(modsFolder).exists()) {
            new File(modsFolder).mkdir();
        }
        Path folderPath = Paths.get(modfolder);
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-xr-x");
        Path base = Paths.get("");
        Set<PosixFilePermission> pem = PosixFilePermissions.fromString("rwxr-xr-x");
        try {
            Files.setPosixFilePermissions(base, pem);
            HaikuLogger.info("Permissions set successfully for folder: " + base);
        } catch (Exception e) {
            HaikuLogger.info("Error while setting permissions for folder: " + e.getMessage());
            e.printStackTrace();
        }
        try {
            Files.setPosixFilePermissions(folderPath, perms);
            HaikuLogger.info("Permissions set successfully for folder: " + folderPath);
        } catch (Exception e) {
            HaikuLogger.info("Error while setting permissions for folder: " + e.getMessage());
            e.printStackTrace();
        }

            // this loads all the mods from the mods.txt file
            // might not even need this because if I was better I could just use the gradle build include to do this too and it would be better but that fine
            // first download the mods.txt file from the github if it does not exist
            if (!new File("mods.txt").exists()) {
                getMod("mods.txt", "https://raw.githubusercontent.com/Robotboy26/minecraftClient/master/cloudFiles//mods.txt", ".");
            }
            try (BufferedReader br = new BufferedReader(new FileReader("mods.txt"))) {
                String line;
                HaikuLogger.logger.info("Loading mods from mods.txt");
                while ((line = br.readLine()) != null) {
                    String url = line.split("|")[0];
                    String group = line.split("|")[1];
                    String[] parts = url.split("/");
                    if (group == "performance") {
                        try {
                            HaikuLogger.logger.info("installing/loading mod: " + parts[parts.length - 1]);
                            getMod(parts[parts.length - 1], line, modsFolder);
                        } catch (Exception e) {
                            HaikuLogger.logger.info("installing/loading mod: " + line);
                            getMod(line, line, modsFolder);
                        }
                    } else {
                        HaikuLogger.logger.info("not installing mod becuase it is not in the wanted group '" + group + "' : " + line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            HaikuLogger.logger.info("Loaded mods!");
            HaikuLogger.logger.info("If this is your first time running haiku, please restart your game!");

            // download shaders (disabled by default but can be enabled from in the client (at somepoint you could make a hack that would enable it)(in like a more vinilla setting))
            if (new File("shaderpacks").exists()) {
                String shaderUrl = "https://www.mediafire.com/file/stwyz8u89eivvq6/kuda-shaders-v6.5.26.zip/file";
                String[] shaderUrlPart = shaderUrl.split("/");
                getMod(shaderUrlPart[shaderUrlPart.length - 2], shaderUrl, "shaderpacks");
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
        } else {
            HaikuLogger.logger.info("Already exists not downloading again! " + modName);
        }
    }

    private static void downloadMod(String url, String modsFolder, String modName) throws IOException {
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
