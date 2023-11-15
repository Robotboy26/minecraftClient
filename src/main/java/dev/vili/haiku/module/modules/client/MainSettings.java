package dev.vili.haiku.module.modules.client;

import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.*;

public final class MainSettings extends Module {
    public static BooleanSetting futureCompatibility = new BooleanSetting("FutureCompatibility", "FutureCompatibility", false);
    public static BooleanSetting customMainMenu = new BooleanSetting("CustomMainMenu", "CustomMainMenu", false);
    public static BooleanSetting renderRotations = new BooleanSetting("RenderRotations", "RenderRotations", false);
    public static BooleanSetting skullEmoji = new BooleanSetting("SkullEmoji", "SkullEmoji", false);
    public static BooleanSetting debug = new BooleanSetting("Debug", "Debug", false);
    // public static Setting<Language> language = new Setting<>("Language", Language.ENG);
    // public static Setting<String> prefix = new Setting<>("Prefix", "@");

    private static MainSettings instance;

    public enum Language {
        RU,
        ENG
    }

    public MainSettings() {
        super("MainSettings", "MainSettings", GLFW.GLFW_KEY_UNKNOWN, Category.CLIENT);
        instance = this;
    }

    // public static boolean isRu() {
    //     return language.getValue() == Language.RU;
    // }

    public static MainSettings getInstance() {
        return instance;
    }
}
