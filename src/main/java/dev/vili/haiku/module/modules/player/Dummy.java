/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.vili.haiku.module.modules.player;

import dev.vili.haiku.module.Module;

import org.lwjgl.glfw.GLFW;

public class Dummy extends Module
{

    public Dummy() {
        super("Dummy", "this is a test(this will crash your game).", GLFW.GLFW_KEY_UNKNOWN, Category.PLAYER);
    }

    @Override
    public void onEnable() {
        try {
            mc.worldRenderer.wait(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
