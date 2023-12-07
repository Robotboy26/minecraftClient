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

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class ConfigManager {
    private static MinecraftClient mc = MinecraftClient.getInstance();
    private final File file;
    private final File mainDirectory;

    public ConfigManager() {
        mainDirectory = new File(mc.runDirectory, "Nebula");

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

                for (Setting<?> setting : module.settings) {
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
        FileInputStream fileInputStream = new FileInputStream(file);
        properties.loadFromXML(fileInputStream);

        for (Module module : Nebula.getInstance().getModuleManager().getModules()) {
            String enabled = properties.getProperty(module.getDisplayName() + ".enabled");
            if (enabled != null) {
                module.setEnabled(Boolean.parseBoolean(enabled));
            }
          
            for (Setting<?> setting : module.settings) {
                String value = properties.getProperty(module.getDisplayName() + "." + setting.getDisplayName());
                
                if (value != null) {
                    switch (setting.getClass().getSimpleName()) {
                        case "BooleanSetting":
                            BooleanSetting booleanSetting = (BooleanSetting) setting;
                            booleanSetting.setValue(Boolean.parseBoolean(value));
                            break;
                        case "ColorSetting":
                            ColorSetting colorSetting = (ColorSetting) setting;
                            colorSetting.setValue(Color.decode(value));
                            break;
                        case "DoubleSetting":
                            DoubleSetting doubleSetting = (DoubleSetting) setting;
                            doubleSetting.setValue(Double.parseDouble(value));
                            break;
                        case "EnumSetting":
                            break;
                        case "IntegerSetting":
                            IntegerSetting integerSetting = (IntegerSetting) setting;
                            integerSetting.setValue(Integer.parseInt(value));
                            break;
                        case "KeybindSetting":
                            KeybindSetting keybindSetting = (KeybindSetting) setting;
                            keybindSetting.setKey(Integer.parseInt(value));
                            break;
                        case "StringSetting":
                            StringSetting stringSetting = (StringSetting) setting;
                            stringSetting.setValue(value);
                            break;
                        default:
                            NebulaLogger.logger.error("Unknown setting type: " + setting.getClass().getSimpleName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            NebulaLogger.logger.error("Error while loading config!", e);
        }
    }
}