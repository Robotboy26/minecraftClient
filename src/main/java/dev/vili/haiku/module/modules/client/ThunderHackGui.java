package dev.vili.haiku.module.modules.client;

import dev.vili.haiku.gui.thundergui.ThunderGui;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.*;

import java.awt.*;

import org.lwjgl.glfw.GLFW;

public final class ThunderHackGui extends Module {
    public static final ColorSetting onColor1 = new ColorSetting("OnColor1", "", 71, 0, 117);
    public static final ColorSetting onColor2 = new ColorSetting("OnColor2", "", 32, 1, 96);
    public static final NumberSetting scrollSpeed = new NumberSetting("ScrollSpeed", "", 1, 0.1, 2.0, 0.01);

    private static ThunderHackGui instance;

    public ThunderHackGui() {
        super("ThunderGui", "ThunderGui", GLFW.GLFW_KEY_P, Category.CLIENT);
        instance = this;
    }

    @Override
    public void onEnable() {
        mc.setScreen(ThunderGui.getThunderGui());
        onDisable();
    }

    public static Color getColorByTheme(int id) {
        return switch (id) {
            case 0 -> new Color(37, 27, 41, 250); // Основная плита
            case 1 -> new Color(50, 35, 60, 250); // плита лого
            case 2 -> new Color(-1); // надпись THUNDERHACK+, белые иконки
            case 3 -> new Color(0x656565); // версия под надписью
            case 4 -> new Color(50, 35, 60, 178); // плита под категориями, выбор режима гуи (выкл)
            case 5 -> new Color(133, 93, 162, 178); // выбор режима гуи (вкл)
            case 6 -> new Color(88, 64, 107, 178); // цвет разделителя качели выбора режима
            case 7 -> new Color(25, 20, 30, 255); // цвет плиты настроек
            case 8 -> new Color(0x656565); // версия под надписью
            case 9 -> new Color(50, 35, 60, 178);
            default -> new Color(37, 27, 41, 250); // плита под категориями
        };
    }

    public static ThunderHackGui getInstance() {
        return instance;
    }
}
