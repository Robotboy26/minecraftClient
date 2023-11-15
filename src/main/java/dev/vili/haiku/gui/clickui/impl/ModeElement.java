package dev.vili.haiku.gui.clickui.impl;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import thunder.hack.gui.clickui.AbstractElement;
import thunder.hack.gui.font.FontRenderers;
import thunder.hack.modules.client.ClickGui;
import thunder.hack.setting.Setting;
import thunder.hack.utility.render.Render2DEngine;
import thunder.hack.utility.render.animation.Animation;
import thunder.hack.utility.render.animation.DecelerateAnimation;
import thunder.hack.utility.render.animation.Direction;

import java.awt.*;

import static thunder.hack.gui.clickui.normal.ClickUI.arrow;

public class ModeElement extends AbstractElement {
    public Setting setting2;
    private boolean open;
    private double wheight;

    private final Animation rotation = new DecelerateAnimation(240, 1, Direction.FORWARDS);

    public ModeElement(Setting setting) {
        super(setting);
        this.setting2 = setting;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        rotation.setDirection(open ? Direction.BACKWARDS : Direction.FORWARDS);

        float tx = (float) (x + width - 11);
        float ty = (float) (y + (wheight / 2));

        MatrixStack matrixStack = context.getMatrices();

        float thetaRotation = (float) (-180f * rotation.getOutput());
        matrixStack.push();

        matrixStack.translate(tx, ty, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(thetaRotation));
        matrixStack.translate(-tx, -ty, 0);

        context.drawTexture(arrow, (int) (x + width - 14), (int) (y + (wheight - 6) / 2), 0, 0, 6, 6, 6, 6);

        matrixStack.pop();

        if(setting.parent != null) {
            Render2DEngine.drawRect(context.getMatrices(), (float) x + 4, (float) y, (float) (1f), 17, ClickGui.getInstance().getColor(1));
        }

        FontRenderers.getSettingsRenderer().drawString(matrixStack, setting2.getName(), (setting.parent != null ? 2f : 0f) + (int) (x + 6), (int) (y + wheight / 2 - (6 / 2f)) + 3, new Color(-1).getRGB());
        FontRenderers.getSettingsRenderer().drawString(matrixStack, setting2.currentEnumName(), (int) (x + width - 16 - FontRenderers.getSettingsRenderer().getStringWidth(setting.currentEnumName())), 3 + (int) (y + wheight / 2 - (6 / 2f)), new Color(-1).getRGB());

        if (open) {
            Color color = ClickGui.getInstance().getColor(0);
            double offsetY = 0;
            for (int i = 0; i <= setting2.getModes().length - 1; i++) {
                FontRenderers.getSettingsRenderer().drawString(matrixStack, setting2.getModes()[i], (int) x + (int) width / 2f - (FontRenderers.getSettingsRenderer().getStringWidth(setting2.getModes()[i]) / 2f), (int) (y + wheight + ((12 >> 1) - (6 / 2f) - 1) + offsetY), setting2.currentEnumName().equalsIgnoreCase(setting2.getModes()[i]) ? color.getRGB() : new Color(-1).getRGB());
                offsetY += 12;
            }
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (Render2DEngine.isHovered(mouseX, mouseY, x, y, width, wheight)) {
            if (button == 0) {
                setting2.increaseEnum();
            } else
                open = !open;
        }

        if (open) {
            double offsetY = 0;
            for (int i = 0; i <= setting2.getModes().length - 1; i++) {
                if (Render2DEngine.isHovered(mouseX, mouseY, x, y + wheight + offsetY, width, 12) && button == 0)
                    setting2.setEnumByNumber(i);
                offsetY += 12;
            }
        }
        super.mouseClicked(mouseX, mouseY, button);
    }


    public void setWHeight(double height) {
        this.wheight = height;
    }

    public boolean isOpen() {
        return open;
    }
}