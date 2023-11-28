/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.nebula.config;

import dev.nebula.Nebula;
import dev.nebula.module.Module;
import dev.nebula.gui.settings.BooleanSetting;
import dev.nebula.gui.settings.ColorSetting;
import dev.nebula.gui.settings.DoubleSetting;
import dev.nebula.gui.settings.EnumSetting;
import dev.nebula.gui.settings.IntegerSetting;
import dev.nebula.gui.settings.KeybindSetting;
import dev.nebula.gui.settings.StringSetting;
import dev.nebula.gui.settings.Setting;
import dev.nebula.utils.NebulaLogger;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class ConfigManager {
    private final File file;
    private final File mainDirectory;

    public ConfigManager() {
        mainDirectory = new File(MinecraftClient.getInstance().runDirectory, "haiku");

        if (!mainDirectory.exists()) {
            mainDirectory.mkdir();
        }

        file = new File(mainDirectory, "config.xml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the config file.
     */
    public File getFile() {
        return file;
    }

    /**
     * Gets the main directory.
     */
    public File getMainDirectory() {
        return mainDirectory;
    }

    /**
     * Saves the config.
     */
    public void save() {
        try {
            NebulaLogger.logger.info("Saving config...");
            Properties properties = new Properties();

            for (Module module : Nebula.getInstance().getModuleManager().getModules()) {
                properties.setProperty(module.getDisplayName() + ".enabled", String.valueOf(module.isEnabled()));

                for (Setting setting : module.settings) {
                    switch (setting.getClass().getSimpleName()) {
                        case "BooleanSetting" -> {
                            BooleanSetting booleanSetting = (BooleanSetting) setting;
                            properties.setProperty(module.getDisplayName() + "." + setting.getDisplayName(), String.valueOf(booleanSetting.getValue()));
                        }
                        case "ColorSetting" -> {
                            ColorSetting colorSetting = (ColorSetting) setting;
                            properties.setProperty(module.getDisplayName() + "." + setting.getDisplayName(), String.valueOf(colorSetting.getValue()));
                        }
                        case "DoubleSetting" -> {
                            DoubleSetting doubleSetting = (DoubleSetting) setting;
                            properties.setProperty(module.getDisplayName() + "." + setting.getDisplayName(), String.valueOf(doubleSetting.getValue()));
                        }
                        case "EnumSetting" -> {
                            EnumSetting<?> enumSetting = (EnumSetting<?>) setting;
                            properties.setProperty(module.getDisplayName() + "." + setting.getDisplayName(), String.valueOf(enumSetting.getValue()));
                        }
                        case "IntegerSetting" -> {
                            IntegerSetting integerSetting = (IntegerSetting) setting;
                            properties.setProperty(module.getDisplayName() + "." + setting.getDisplayName(), String.valueOf(integerSetting.getValue()));
                        }
                        case "KeybindSetting" -> {
                            KeybindSetting keybindSetting = (KeybindSetting) setting;
                            properties.setProperty(module.getDisplayName() + "." + setting.getDisplayName(), String.valueOf(keybindSetting.getKey()));
                        }
                        case "StringSetting" -> {
                            StringSetting stringSetting = (StringSetting) setting;
                            properties.setProperty(module.getDisplayName() + "." + setting.getDisplayName(), String.valueOf(stringSetting.getValue()));
                        }
                        default ->
                                NebulaLogger.logger.error("Unknown setting type: " + setting.getClass().getSimpleName());
                    }
                }

                properties.storeToXML(new FileOutputStream(file), null); // Save the config.
            }
        } catch (Exception e) {
            NebulaLogger.logger.error("Error while saving config!", e);
        }
    }

    /**
     * Loads the config.
     */
    public void load() {
        try {
            NebulaLogger.logger.info("Loading config...");
            Properties properties = new Properties();
            properties.loadFromXML(new FileInputStream(file));

            for (Module module : Nebula.getInstance().getModuleManager().getModules()) {
                if (Boolean.parseBoolean(properties.getProperty(module.getDisplayName() + ".enabled")) != module.isEnabled())
                    module.setEnabled(Boolean.parseBoolean(properties.getProperty(module.getDisplayName() + ".enabled"))); // Set the enabled state.

                for (Setting setting : module.settings) {
                    switch (setting.getClass().getSimpleName()) {
                        case "BooleanSetting" -> {
                            BooleanSetting booleanSetting = (BooleanSetting) setting;
                            if (Boolean.parseBoolean(properties.getProperty(module.getDisplayName() + "." + setting.getDisplayName())) != booleanSetting.isEnabled())
                                booleanSetting.setEnabled(Boolean.parseBoolean(properties.getProperty(module.getDisplayName() + "." + setting.getDisplayName())));
                        }
                        case "NumberSetting" -> {
                            NumberSetting numberSetting = (NumberSetting) setting;
                            if (Double.parseDouble(properties.getProperty(module.getDisplayName() + "." + setting.getDisplayName())) != numberSetting.getValue())
                                numberSetting.setValue(Double.parseDouble(properties.getProperty(module.getDisplayName() + "." + setting.getDisplayName())));
                        }
                        case "StringSetting" -> {
                            StringSetting stringSetting = (StringSetting) setting;
                            if (!properties.getProperty(module.getDisplayName() + "." + setting.getDisplayName()).equals(stringSetting.getString()))
                                stringSetting.setString(properties.getProperty(module.getDisplayName() + "." + setting.getDisplayName()));
                        }
                        case "ModeSetting" -> {
                            ModeSetting modeSetting = (ModeSetting) setting;
                            if (!properties.getProperty(module.getDisplayName() + "." + setting.getDisplayName()).equals(modeSetting.getMode()))
                                modeSetting.setMode(properties.getProperty(module.getDisplayName() + "." + setting.getDisplayName()));
                        }
                        case "KeybindSetting" -> {
                            if (properties.getProperty(module.getDisplayName() + ".key") != null)
                                module.setKey(Integer.parseInt(properties.getProperty(module.getDisplayName() + ".key"))); // Set the key.
                        }
                        default ->
                                NebulaLogger.logger.error("Unknown setting type: " + setting.getClass().getSimpleName());
                    }
                }
            }
        } catch (Exception e) {
            NebulaLogger.logger.error("Error while loading config!", e);
        }
    }
}