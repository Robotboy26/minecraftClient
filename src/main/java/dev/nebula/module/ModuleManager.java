package dev.nebula.module;

import dev.nebula.module.modules.HUD.*;

public class ModuleManager {
    public ModuleManager() {
        Category.init();

        // Combat modules

        // Exploits modules

        // HUD modules
        Category.HUD.modules.add(new TabGUIModule());
		Category.HUD.modules.add(new WatermarkModule());
		Category.HUD.modules.add(new LogoModule());
        Category.HUD.modules.add(new Test());

        // Miscellaneous modules

        // Movement modules

        // Other modules
        Category.OTHER.modules.add(new ClickGUIModule());
		Category.OTHER.modules.add(new HUDEditorModule());

        // Render modules

        // World modules
    }
}
