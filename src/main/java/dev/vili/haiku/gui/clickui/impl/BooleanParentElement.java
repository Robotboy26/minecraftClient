package dev.vili.haiku.gui.clickui.impl;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

import java.awt.*;

import static thunder.hack.gui.clickui.normal.ClickUI.arrow;

public class BooleanParentElement extends AbstractElement {
    private final Setting<BooleanParent> parentSetting;

    public BooleanParentElement(Setting setting) {
        super(setting);
        this.parentSetting = setting;
    }

    private final Animation rotation = new DecelerateAnimation(240, 1, Direction.FORWARDS);
    float animation = 0f;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context,mouseX,mouseY,delta);

        MatrixStack matrixStack = context.getMatrices();

        rotation.setDirection(getParentSetting().getValue().isExtended() ? Direction.BACKWARDS : Direction.FORWARDS);
        float tx = (float) (x + width - 11);
        float ty = (float) (y + (17 / 2));

        matrixStack.push();
        matrixStack.translate(tx, ty, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) (-180f * rotation.getOutput())));
        matrixStack.translate(-tx, -ty, 0);
        context.drawTexture(arrow, (int) (x + width - 14), (int) (y + (17 - 6) / 2), 0, 0, 6, 6, 6, 6);
        matrixStack.pop();

        FontRenderers.getSettingsRenderer().drawString(matrixStack, setting.getName() ,(int) (x + 6), (y + height / 2 - (6 / 2f)) + 2, new Color(-1).getRGB());

        animation = fast(animation, getParentSetting().getValue().isEnabled() ? 1 : 0, 15f);
        double paddingX = 7 * animation;
        Color color = ClickGui.getInstance().getColor(0);
        Render2DEngine.drawRound(context.getMatrices(),(float) (x + width - 36), (float) (y + height / 2 - 4), 15, 8, 4, paddingX > 4 ? color : new Color(0xFFB2B1B1));
        Render2DEngine.drawRound(context.getMatrices(),(float) (x + width - 35 + paddingX), (float) (y + height / 2 - 3), 6, 6, 3, new Color(-1));

    }

    public static double deltaTime() {
        return FrameRateCounter.INSTANCE.getFps() > 0 ? (1.0000 / FrameRateCounter.INSTANCE.getFps()) : 1;
    }

    public static float fast(float end, float start, float multiple) {
        return (1 - MathUtility.clamp((float) (deltaTime() * multiple), 0, 1)) * end + MathUtility.clamp((float) (deltaTime() * multiple), 0, 1) * start;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (hovered) {
            if(button == 0) getParentSetting().getValue().setEnabled(!getParentSetting().getValue().isEnabled());
            else getParentSetting().getValue().setExtended(!getParentSetting().getValue().isExtended());
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    public Setting<BooleanParent> getParentSetting() {
        return parentSetting;
    }
}
