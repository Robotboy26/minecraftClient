/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.vili.haiku.module;

import dev.vili.haiku.Haiku;
import dev.vili.haiku.event.events.KeyEvent;
import dev.vili.haiku.eventbus.HaikuSubscribe;
import dev.vili.haiku.module.modules.combat.Criticals;
//import dev.vili.haiku.module.modules.combat.*;
import dev.vili.haiku.module.modules.movement.*;
import dev.vili.haiku.module.modules.player.*;
import dev.vili.haiku.module.modules.render.*;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public final ArrayList<Module> modules;

    public ModuleManager() {
        modules = new ArrayList<>();

        /* Add modules here */
        /* Combat */
        modules.add(new Criticals());

        /* Movement */
        modules.add(new AutoSprint());
        modules.add(new AutoWalk());
        modules.add(new BoatFly());
        modules.add(new BunnyHop());
        modules.add(new ClickTP());
        modules.add(new EntityControl());
        modules.add(new Fly());
        modules.add(new Jetpack());
        modules.add(new NoFall());
        modules.add(new NoSlow());
        modules.add(new SafeWalk());
        modules.add(new Speed());
        modules.add(new Spider());
        modules.add(new Step());

        /* Player */
        modules.add(new AntiHunger());
        modules.add(new AntiWither());
        modules.add(new AutoLeave());
        modules.add(new Dummy());
        modules.add(new Invisability());
        modules.add(new Invulnerable());
        modules.add(new QuickDisconnect());

        /* Render */
        modules.add(new AntiBlind());
        modules.add(new BetterTab());
        modules.add(new Breadcrumbs());
        modules.add(new Bright());
        modules.add(new FullBright());
        modules.add(new Gui());
        modules.add(new Hud());
        modules.add(new NoBackground());
        modules.add(new OneGui());
        modules.add(new TrueSight());
    }

    /**
     * Gets the modules.
     */
    public ArrayList<Module> getModules() {
        return modules;
    }

    /**
     * Gets enabled modules.
     */
    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<>();
        for (Module module : modules) {
            if (module.isEnabled())
                enabledModules.add(module);
        }
        return enabledModules;
    }

    /**
     * Gets the module by name.
     *
     * @param name name of the module
     */
    public Module getModule(String name) {
        return modules.stream().filter(mm -> mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Gets the modules state
     *
     * @param name name of the module
     */
    public boolean isModuleEnabled(String name) {
        Module mod = modules.stream().filter(mm -> mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        return mod.isEnabled();
    }

    /**
     * Gets the modules by category.
     *
     * @param category category of the module
     */
    public List<Module> getModulesByCategory(Module.Category category) {
        List<Module> cats = new ArrayList<>();
        for (Module m : modules) {
            if (m.getCategory() == category) cats.add(m);
        }
        return cats;
    }

    @HaikuSubscribe
    public void onKeyPress(KeyEvent event) {
        if (InputUtil.isKeyPressed(Haiku.mc.getWindow().getHandle(), GLFW.GLFW_KEY_F3)) return;
        modules.stream().filter(m -> m.getKey() == event.getKey()).forEach(Module::toggle);
    }
}
