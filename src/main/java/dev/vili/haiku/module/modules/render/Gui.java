/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.vili.haiku.module.modules.render;

import dev.vili.haiku.gui.HaikuGui;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.NumberSetting;

import org.lwjgl.glfw.GLFW;

public class Gui extends Module {

    public final NumberSetting red = new NumberSetting("Red", "Red.", 0, 0, 255, 1);
    public final NumberSetting green = new NumberSetting("Green", "Green.", 0, 0, 255, 1);
    public final NumberSetting blue = new NumberSetting("Blue", "Blue.", 0, 0, 255, 1);


    public Gui() {
        super("Gui", "Haiku gui.", GLFW.GLFW_KEY_RIGHT_SHIFT, Category.RENDER);
        this.addSettings(red, green, blue);
    }
    
    @Override
    public void toggle() {
        mc.setScreen(new HaikuGui());
    }

}
