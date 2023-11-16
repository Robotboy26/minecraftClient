package dev.vili.haiku.gui.thundergui.components;

import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

import dev.vili.haiku.gui.font.FontRenderers;
import dev.vili.haiku.setting.Setting;
import dev.vili.haiku.utils.thunderClient.utility.render.Render2DEngine;

public class ParentComponent extends SettingElement {

    public ParentComponent(Setting setting) {
        super(setting);
        // Parent parent = (Parent) setting.getValue();
        // parent.setExtended(true);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack,mouseX, mouseY, partialTicks);
        // if ((getY() > ThunderGui.getInstance().main_posY + ThunderGui.getInstance().height) || getY() < ThunderGui.getInstance().main_posY) {
        //     return;
        // }
        FontRenderers.modules.drawCenteredString(stack,getSetting().getName(), (float) (getX() + width / 2f), (float) getY() + 2, new Color(0xB0FFFFFF, true).getRGB());
        Render2DEngine.draw2DGradientRect(stack,(float) (getX() + 10), (float) (getY() + 6), (float) ((getX() + width / 2f) - 20), (float) (getY() + 7), new Color(0x0FFFFFF, true), new Color(0x0FFFFFF, true), new Color(0xB0FFFFFF, true), new Color(0xB0FFFFFF, true));
        Render2DEngine.draw2DGradientRect(stack,(float) (getX() + width / 2f + 20f), (float) (getY() + 6), (float) (getX() + width - 10), (float) (getY() + 7), new Color(0xB0FFFFFF, true), new Color(0xB0FFFFFF, true), new Color(0x0FFFFFF, true), new Color(0x0FFFFFF, true));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        // if ((getY() > ThunderGui.getInstance().main_posY + ThunderGui.getInstance().height) || getY() < ThunderGui.getInstance().main_posY) {
        //     return;
        // }
        if (hovered) {
            // Parent parent = (Parent) setting.getValue();
            // parent.setExtended(!parent.isExtended());
        }
    }
}