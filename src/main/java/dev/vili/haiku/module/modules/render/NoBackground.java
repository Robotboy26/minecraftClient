/*
 * Copyright (c) 2014-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.module.modules.render;

import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

import org.lwjgl.glfw.GLFW;

public final class NoBackground extends Module
{
    public final BooleanSetting allGuis = new BooleanSetting("All GUIs",
        "Removes the background for all GUIs, not just inventories.", false);
    
    public NoBackground()
    {
        super("NoBackground", "Removes the background from GUIs.", GLFW.GLFW_KEY_UNKNOWN, Category.RENDER);
        this.addSettings(allGuis);
    }
    
    @Override
    public void onEnable()
    {
        shouldCancelBackground(mc.currentScreen);
        super.onEnable();
    }

    @Override
    public void onDisable()
    {
        super.onDisable();
    }

    public boolean shouldCancelBackground(Screen screen)
    {
        if(!isEnabled())
            return false;
        
        if(mc.world == null)
            return false;
        
        if(!allGuis.isEnabled() && !(screen instanceof HandledScreen))
            return false;
        
        return true;
    }
    
    // See ScreenMixin.onRenderBackground()
}
