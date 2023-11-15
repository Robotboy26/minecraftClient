package dev.vili.haiku.module.modules.client;

import meteordevelopment.orbit.EventHandler;
import dev.babbaj.pathfinder.xz.p;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.*;

import java.awt.*;

public class ClickGui extends Module {
    private static ClickGui INSTANCE = new ClickGui();

    public final ModeSetting mode = new ModeSetting("Mode", "what mode to use", "default", "small");
    public final ModeSetting colorMode = new ModeSetting("colorMode", "what color mode to use", "default", "default", "static", "sky", "lightRainbow", "rainbow", "fade", "doubleColor", "analogous");
    public final ModeSetting scrollMode = new ModeSetting("scrollMode", "what scroll mode to use", "default", "new", "old");
    public final NumberSetting colorSpeed = new NumberSetting("colorSpeed", "how fast the color changes", 100, 1, 1000, 1);
    public final NumberSetting scrollSpeed = new NumberSetting("scrollSpeed", "how fast the scroll changes", 100, 1, 1000, 1);
    public final ColorSetting hcolor1 = new ColorSetting("hcolor1", "color 1", new Color(0, 0, 0, 255));
    public final ColorSetting acolor = new ColorSetting("acolor", "color 2", new Color(0, 0, 0, 255));
    public final BooleanSetting rainbow = new BooleanSetting("rainbow", "rainbow", false);
    public final BooleanSetting blur = new BooleanSetting("blur", "blur", false);
    public final BooleanSetting blurBackground = new BooleanSetting("blurBackground", "blurBackground", false);
    public final BooleanSetting blurSlots = new BooleanSetting("blurSlots", "blurSlots", false);
    public final BooleanSetting blurFont = new BooleanSetting("blurFont", "blurFont", false);


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

