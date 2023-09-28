/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.vili.haiku.module.modules.render;

import dev.vili.haiku.gui.HaikuOneGui;
import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.ColorSetting;

import org.lwjgl.glfw.GLFW;

public class OneGui extends Module {

    public static final ColorSetting color = new ColorSetting("Color-OneGui", "ummmm color test this acually does nothing right now", 0.5f, 0.5f, 0.5f);

    public OneGui() {
        super("OneGui", "One window, no tabs.", GLFW.GLFW_KEY_RIGHT_CONTROL, Category.RENDER);
        this.addSettings(color);
    }

    @Override
    public void toggle() {
        mc.setScreen(new HaikuOneGui(color.color()));
    }

}
