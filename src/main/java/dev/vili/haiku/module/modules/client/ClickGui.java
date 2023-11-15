package dev.vili.haiku.module.modules.client;

import meteordevelopment.orbit.EventHandler;
import thunder.hack.events.impl.SettingEvent;
import thunder.hack.gui.clickui.normal.ClickUI;
import thunder.hack.gui.clickui.small.SmallClickUI;
import thunder.hack.modules.Module;
import thunder.hack.setting.impl.ColorSetting;
import thunder.hack.setting.Setting;
import thunder.hack.utility.render.Render2DEngine;

import java.awt.*;

public class ClickGui extends Module {
    private static ClickGui INSTANCE = new ClickGui();

    public Setting<Mode> mode = new Setting<>("Mode", Mode.Default);

    public Setting<scrollModeEn> scrollMode = new Setting<>("ScrollMode", scrollModeEn.Old);
    public Setting<Integer> catHeight = new Setting<>("CategoryHeight", 300, 100, 720);

    private final Setting<colorModeEn> colorMode = new Setting<>("ColorMode", colorModeEn.Static);
    public final Setting<ColorSetting> hcolor1 = new Setting<>("Main", new ColorSetting(-6974059));
    public final Setting<ColorSetting> acolor = new Setting<>("Main2", new ColorSetting(-8365735));
    public final Setting<ColorSetting> plateColor = new Setting<>("Plate", new ColorSetting(-14474718));
    public final Setting<ColorSetting> disabled = new Setting<>("Disabled", new ColorSetting(new Color(24, 24, 27)));
    public final Setting<ColorSetting> catColor = new Setting<>("Category", new ColorSetting(-15395563));
    public Setting<Integer> colorSpeed = new Setting<>("ColorSpeed", 18, 2, 54);
    public Setting<Boolean> showBinds = new Setting<>("ShowBinds", true);
    public Setting<Boolean> outline = new Setting<>("Outline", false);
    public Setting<Boolean> descriptions = new Setting<>("Descriptions", true);
    public Setting<Boolean> msaa = new Setting<>("MSAA", true);

/*
    я хотел, а потом опять забил
    private final Setting<PositionSetting> combatCat = new Setting<>("combatCat", new PositionSetting(0.5f, 0.5f));
    private final Setting<PositionSetting> miscCat = new Setting<>("miscCat", new PositionSetting(0.5f, 0.5f));
    private final Setting<PositionSetting> renderCat = new Setting<>("renderCat", new PositionSetting(0.5f, 0.5f));
    private final Setting<PositionSetting> movementCat = new Setting<>("movementCat", new PositionSetting(0.5f, 0.5f));
    private final Setting<PositionSetting> playerCat = new Setting<>("playerCat", new PositionSetting(0.5f, 0.5f));
    private final Setting<PositionSetting> clientCat = new Setting<>("clientCat", new PositionSetting(0.5f, 0.5f));
    private final Setting<PositionSetting> hudCat = new Setting<>("hudCat", new PositionSetting(0.5f, 0.5f));
 */

    public ClickGui() {
        super("ClickGui", Module.Category.CLIENT);
        this.setInstance();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    public Color getColor(int count) {
        return switch (colorMode.getValue()) {
            case Sky -> Render2DEngine.skyRainbow(colorSpeed.getValue(), count);
            case LightRainbow -> Render2DEngine.rainbow(colorSpeed.getValue(), count, .6f, 1, 1);
            case Rainbow -> Render2DEngine.rainbow(colorSpeed.getValue(), count, 1f, 1, 1);
            case Fade -> Render2DEngine.fade(colorSpeed.getValue(), count, hcolor1.getValue().getColorObject(), 1);
            case DoubleColor -> Render2DEngine.interpolateColorsBackAndForth(colorSpeed.getValue(), count,
                    hcolor1.getValue().getColorObject(), new Color(0xFFFFFFFF), true);
            case Analogous -> Render2DEngine.interpolateColorsBackAndForth(colorSpeed.getValue(), count, hcolor1.getValue().getColorObject(), Render2DEngine.getAnalogousColor(acolor.getValue().getColorObject()), true);
            default -> hcolor1.getValue().getColorObject();
        };
    }

    @Override
    public void onEnable() {
        setGui();
    }

    public void setGui() {
        if(mode.getValue() == Mode.Default) mc.setScreen(ClickUI.getClickGui());
        else mc.setScreen(SmallClickUI.getClickGui());
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @EventHandler
    public void onSettingChange(SettingEvent e) {
        if(e.getSetting() == mode) {
             setGui();
        }
    }

    @Override
    public void onUpdate() {
        if (!(ClickGui.mc.currentScreen instanceof ClickUI) && !(ClickGui.mc.currentScreen instanceof SmallClickUI)) disable();
    }

    public enum colorModeEn {
        Static,
        Sky,
        LightRainbow,
        Rainbow,
        Fade,
        DoubleColor,
        Analogous
    }

    public enum scrollModeEn {
        New,
        Old
    }

    public enum Mode {
        Default,
        Small
    }
}

