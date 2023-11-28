package dev.nebula.module;

import java.util.stream.Stream;

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

    // get all modules
    public Module[] getModules() {
        return Stream.of(Category.values()).flatMap(category -> category.modules.stream()).toArray(Module[]::new);
    }

    /**
     * Gets the module by name.
     */
    public Module getModuleByName(String name) {
        for (Category category : Category.values()) {
            for (Module module : category.modules) {
                if (module.getDisplayName().equalsIgnoreCase(name)) {
                    return module;
                }
            }
        }

        return null;
    }
}