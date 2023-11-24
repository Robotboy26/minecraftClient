/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.phantom;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.lwjgl.glfw.GLFW;

import dev.phantom.command.CommandManager;
import dev.phantom.gui.ClickGUI;
import dev.phantom.gui.module.Category;
import dev.phantom.gui.module.ClickGUIModule;
import dev.phantom.gui.module.HUDEditorModule;
import dev.phantom.gui.module.LogoModule;
import dev.phantom.gui.module.TabGUIModule;
import dev.phantom.gui.module.WatermarkModule;

import dev.phantom.utils.DownloadUtils;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

/**
 * Main class for haiku.
 */
public class Phantom implements ModInitializer {
    public static final String MOD_NAME = "Phantom";
    public static final String MOD_VERSION = "0.1";
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    //private final ModuleManager MODULE_MANAGER = new ModuleManager();
    private final CommandManager COMMAND_MANAGER = new CommandManager();
    private static Phantom INSTANCE;
    private Path haikuFolder;
    public static long initTime;

    // gui stuff
    private static ClickGUI GUI;
	private boolean GUIenabled = false;
	private final boolean keys[] = new boolean[266];

    public Phantom() {
        INSTANCE = this;
    }

    /**
     * Gets the instance of Haiku.
     */
    public static Phantom getInstance() {
        return INSTANCE;
    }

    /**
     * Called when haiku is initialized.
     */
    @Override
    public void onInitialize() {
        initTime = System.currentTimeMillis();
		haikuFolder = createHaikuFolder();

        // init gui
        Category.init();
		Category.OTHER.modules.add(new ClickGUIModule());
		Category.OTHER.modules.add(new HUDEditorModule());
		Category.HUD.modules.add(new TabGUIModule());
		Category.HUD.modules.add(new WatermarkModule());
		Category.HUD.modules.add(new LogoModule());
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (!GUIenabled) {
				for (int i=32;i<keys.length;i++) keys[i]=GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(),i)==GLFW.GLFW_PRESS;
				GUI=new ClickGUI();
				HudRenderCallback.EVENT.register((cli,tickDelta)->GUI.render());
				GUIenabled=true;
			}
			for (int i=32;i<keys.length;i++) {
				if (keys[i]!=(GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(),i)==GLFW.GLFW_PRESS)) {
					keys[i]=!keys[i];
					if (keys[i]) {
						if (i==ClickGUIModule.keybind.getKey()) GUI.enterGUI();
						if (i==HUDEditorModule.keybind.getKey()) GUI.enterHUDEditor();
						GUI.handleKeyEvent(i);
					}
				}
            }
        });   

        // loading mods "settings"
        Boolean installMods = false;
        String modfolder = "mods";

        if (installMods) {
            DownloadUtils.init(modfolder);
            // download shaders (disabled by default but can be enabled from in the client (at somepoint you could make a hack that would enable it)(in like a more vinilla setting))
            DownloadUtils.getShader();
            }
        }

            //Path altsFile = haikuFolder.resolve("alts.encrypted_json");
            //Path encFolder = Encryption.chooseEncryptionFolder();
            //altManager = new AltManager(altsFile, encFolder);

            //HaikuLogger.logger.info(MOD_NAME + " v" + MOD_VERSION + " (phase 1) has initialized!");
            //CONFIG_MANAGER.load();
            //HaikuLogger.logger.info("Loaded config!");

        // Save configs on shutdown
    //     ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
    //         //CONFIG_MANAGER.save();
    //         //HaikuLogger.logger.info("Saved config!");
    //     });
    // }

    private Path createHaikuFolder()
	{
		Path dotMinecraftFolder = mc.runDirectory.toPath().normalize();
		Path haikuFolder = dotMinecraftFolder.resolve("haiku");
		
		try
		{
			Files.createDirectories(haikuFolder);
			
		}catch(IOException e)
		{
			throw new RuntimeException(
				"Couldn't create .minecraft/haiku folder.", e);
		}
		
		return haikuFolder;
	}

    public Path getHaikuFolder()
	{
		return haikuFolder;
	}

    /**
     * Gets the event bus.
     */
    //public EventBus getEventBus() {
    //    return EVENT_BUS;
    //}

    /**
     * Gets the module manager.
     */
    //public ModuleManager getModuleManager() {
    //    return MODULE_MANAGER;
    //}

    /**
     * Gets the command manager.
     */
    public CommandManager getCommandManager() {
        return COMMAND_MANAGER;
    }

    /**
     * Gets the setting manager.
     */
    //public SettingManager getSettingManager() {
    //    return SETTING_MANAGER;
    //}

    /**
     * Gets the config manager.
     */
    //public ConfigManager getConfigManager() {
    //    return CONFIG_MANAGER;
    //}
}
