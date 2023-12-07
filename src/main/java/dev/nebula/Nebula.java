/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.nebula;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.lwjgl.glfw.GLFW;

import dev.nebula.command.CommandManager;
import dev.nebula.config.ConfigManager;
import dev.nebula.eventbus.EventBus;
import dev.nebula.gui.ClickGUI;
import dev.nebula.module.ModuleManager;
import dev.nebula.module.modules.HUD.ClickGUIModule;
import dev.nebula.module.modules.HUD.HUDEditorModule;
import dev.nebula.utils.DownloadUtils;
import dev.nebula.utils.NebulaLogger;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;

/**
 * Main class for haiku.
 */
public class Nebula implements ModInitializer {
    public static final String MOD_NAME = "Nebula Client";
    public static final String MOD_VERSION = "0.1";
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    private final EventBus EVENT_BUS = new EventBus();
    private final ModuleManager MODULE_MANAGER = new ModuleManager();
    private final CommandManager COMMAND_MANAGER = new CommandManager();
    private final ConfigManager CONFIG_MANAGER = new ConfigManager();
    private static Nebula INSTANCE;
    private Path NebulaFolder;
    public static long initTime;

    // gui stuff
    private static ClickGUI GUI;
	private boolean GUIenabled = false;
	private final boolean keys[] = new boolean[266];

    public Nebula() {
        INSTANCE = this;
    }

    /**
     * Gets the instance of Nebula.
     */
    public static Nebula getInstance() {
        return INSTANCE;
    }

    /**
     * Called when Nebula is initialized.
     */

    @Override
    public void onInitialize() {
        NebulaLogger.info("Starting " + MOD_NAME + " version: " + MOD_VERSION);
        initTime = System.currentTimeMillis();
        NebulaFolder = createNebulaFolder();

        // init gui
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!GUIenabled) {
                for (int i = 32; i<keys.length; i++) keys[i] = GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(),i) == GLFW.GLFW_PRESS;
                GUI = new ClickGUI();
                HudRenderCallback.EVENT.register((cli,tickDelta) -> GUI.render());
                GUIenabled = true;
            }
            if (mc.currentScreen instanceof ChatScreen) {
                // Disable keybinds when chat is open
                return;
            }
            for (int i = 32; i<keys.length; i++) {
                if (keys[i] != (GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(),i) == GLFW.GLFW_PRESS)) {
                    keys[i] =! keys[i];
                    if (keys[i]) {
                        if (i == ClickGUIModule.keybind.getKey()) GUI.enterGUI();
                        if (i == HUDEditorModule.keybind.getKey()) GUI.enterHUDEditor();
                        GUI.handleKeyEvent(i);
                    }
                }
            }
        });   

        // loading mods "settings"
        Boolean installMods = false;
        Boolean cloud = true;
        String modfolder = "mods";

        if (installMods) {
            NebulaLogger.info("Installing mods...");
            DownloadUtils.init(modfolder);
            DownloadUtils.downloadFromFile(modfolder, cloud);
            DownloadUtils.getBaritone(cloud);
            DownloadUtils.getShader();
            NebulaLogger.info("Are mods intalled and initiated " + DownloadUtils.modInstalled());
        }
    }

    /**
     * Called when Nebula is stopped.
     */
    // save the config
    // ClientLifecycleEvents.CLIENT_STOPPING.@register((client) -> {
    //         CONFIG_MANAGER.save();
    //         NebulaLogger.logger.info("Configurations saved successfully.");
    // });


    private Path createNebulaFolder()
	{
		Path dotMinecraftFolder = mc.runDirectory.toPath().normalize();
		Path NebulaFolder = dotMinecraftFolder.resolve("Nebula");
		
		try
		{
			Files.createDirectories(NebulaFolder);
			
		}catch(IOException e)
		{
			throw new RuntimeException(
				"Couldn't create nebula folder.", e);
		}
		
		return NebulaFolder;
	}

    public Path getNebulaFolder()
	{
		return NebulaFolder;
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
     * Gets the config manager.
     */
    public ConfigManager getConfigManager() {
       return CONFIG_MANAGER;
    }
}
