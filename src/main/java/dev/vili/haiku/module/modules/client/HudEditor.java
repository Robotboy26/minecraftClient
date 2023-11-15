package dev.vili.haiku.module.modules.client;

import dev.vili.haiku.gui.hud.HudEditorGui;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.Setting;
import dev.vili.haiku.setting.settings.ColorSetting;
import dev.vili.haiku.setting.settings.KeybindSetting;
import dev.vili.haiku.setting.settings.ModeSetting;
import dev.vili.haiku.utils.render.RenderUtils;
import dev.vili.haiku.utils.thunderClient.utility.render.Render2DEngine;

import java.awt.*;

public final class HudEditor extends Module {
    public static final KeybindSetting keybind = new KeybindSetting(0);
    public static final ColorSetting hcolor1 = new ColorSetting("Color1", "Color1", 0.5f, 0.5f, 0.5f);
    public static final ColorSetting acolor = new ColorSetting("Color2", "Color2", 0.5f, 0.5f, 0.5f);
    public static final ColorSetting colorSpeed = new ColorSetting("ColorSpeed", "ColorSpeed", 1, 1, 1);
    public static final ModeSetting colorMode = new ModeSetting("ColorMode", "ColorMode", "Sky", "Sky", "LightRainbow", "Rainbow", "Fade", "DoubleColor");

    private static HudEditor instance;
    public static Object glow;

    public HudEditor() {
        super("HudEditor", "HudEditor", 0, Category.RENDER);
        instance = this;
    }

    public static Color getColor(int count) {
        return switch (colorMode.getMode().toLowerCase()) {
            case "sky" -> Render2DEngine.skyRainbow(colorSpeed.getRGBArray(), count);
            case "lightrainbow" -> Render2DEngine.rainbow(colorSpeed.getRGBArray(), count, 0.6f, 1.0f, 1.0f);
            case "rainbow" -> Render2DEngine.rainbow(colorSpeed.getRGBArray(), count, 1.0f, 1.0f, 1.0f);
            case "fade" -> Render2DEngine.fade(colorSpeed.getRGBArray(), count, new Color(hcolor1.getRGBArray()[0], hcolor1.getRGBArray()[1], hcolor1.getRGBArray()[2]), 1);
            case "doublecolor" -> Render2DEngine.TwoColoreffect(new Color(hcolor1.getRGBArray()[0], hcolor1.getRGBArray()[1], hcolor1.getRGBArray()[2]), new Color(acolor.getRGBArray()[0], acolor.getRGBArray()[1], acolor.getRGBArray()[2]), Math.abs(System.currentTimeMillis() / 10) / 100.0 + (count));
            default -> new Color(hcolor1.getRGBArray()[0], hcolor1.getRGBArray()[1], hcolor1.getRGBArray()[2]);
        };
    }


    @Override
    public void onEnable() {
        mc.setScreen(HudEditorGui.getHudGui());
        onDisable();
    }

    public static HudEditor getInstance() {
        return instance;
    }
}
