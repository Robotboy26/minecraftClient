package dev.vili.haiku.gui.clickui.impl;

import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import dev.vili.haiku.gui.clickui.AbstractElement;
import dev.vili.haiku.gui.font.FontRenderers;
import dev.vili.haiku.setting.Setting;

import java.awt.*;

public class BindElement extends AbstractElement {
    public BindElement(Setting setting) {
        super(setting);
    }

    public boolean isListening;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context,mouseX,mouseY,delta);
        if (this.isListening) {
            FontRenderers.getSettingsRenderer().drawString(context.getMatrices(),"...", (int) (x + 6), (int) (y + height / 2 - (6 / 2f)), new Color(-1).getRGB());
        } else {
            FontRenderers.getSettingsRenderer().drawString(context.getMatrices(),setting.getName() + " " + ((Bind)setting.getValue()).getBind(), (int) (x + 6), (int) (y + height / 2 - (6 / 2f)), new Color(-1).getRGB());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isListening) {
            Bind b = new Bind(button,true,false);
            setting.setValue(b);
            isListening = false;
        }
        if (hovered && button == 0) isListening = !isListening;

        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void keyTyped(int keyCode) {
        if (isListening) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_SPACE || keyCode == GLFW.GLFW_KEY_DELETE) {
                Bind b = new Bind(-1,false,false);
                setting.setValue(b);
            } else {
                Bind b = new Bind(keyCode,false,false);
                setting.setValue(b);
            }
            isListening = false;
        }
    }
}
