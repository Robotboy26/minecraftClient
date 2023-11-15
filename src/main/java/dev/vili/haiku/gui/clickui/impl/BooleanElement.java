package dev.vili.haiku.gui.clickui.impl;

import net.minecraft.client.gui.DrawContext;

import java.awt.*;

import dev.vili.haiku.gui.clickui.AbstractElement;
import dev.vili.haiku.setting.Setting;
import dev.vili.haiku.utils.thunderClient.utility.render.Render2DEngine;

public class BooleanElement extends AbstractElement {
    public BooleanElement(Setting setting) {super(setting);}

    float animation = 0f;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context,mouseX, mouseY, delta);
        animation = fast(animation, (boolean) setting.getValue() ? 1 : 0, 15f);
        double paddingX = 7 * animation;
        Color color = ClickGui.getInstance().getColor(0);
        Render2DEngine.drawRound(context.getMatrices(),(float) (x + width - 21), (float) (y + height / 2 - 4), 15, 8, 4, paddingX > 4 ? color : new Color(0xFFB2B1B1));
        Render2DEngine.drawRound(context.getMatrices(),(float) (x + width - 20 + paddingX), (float) (y + height / 2 - 3), 6, 6, 3, new Color(-1));

        if(setting.parent != null) {
            Render2DEngine.drawRect(context.getMatrices(), (float) x + 4, (float) y, (float) (1f), 15, ClickGui.getInstance().getColor(1));
        }

        FontRenderers.settings.drawString(context.getMatrices(),setting.getName(), (setting.parent != null ? 2f : 0f) + (x + 6), (y + height / 2 - (6 / 2f)) + 2, new Color(-1).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (hovered && button == 0) {
            setting.setValue(!((Boolean) setting.getValue()));
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    public static double deltaTime() {
        return FrameRateCounter.INSTANCE.getFps() > 0 ? (1.0000 / FrameRateCounter.INSTANCE.getFps()) : 1;
    }

    public static float fast(float end, float start, float multiple) {
        return (1 - MathUtility.clamp((float) (deltaTime() * multiple), 0, 1)) * end + MathUtility.clamp((float) (deltaTime() * multiple), 0, 1) * start;
    }
}