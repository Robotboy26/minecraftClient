package dev.nebula.module.modules.HUD;

import org.lwjgl.glfw.GLFW;

import dev.nebula.gui.settings.BooleanSetting;
import dev.nebula.gui.settings.KeybindSetting;
import dev.nebula.module.Module;

public class HUDEditorModule extends Module {
	public static final BooleanSetting showHUD = new BooleanSetting("Show HUD Panels", "showHUD", "Whether to show the HUD panels in the ClickGUI.", ()->true, true);
	public static final KeybindSetting keybind = new KeybindSetting("Keybind", "keybind", "The key to toggle the module.", ()->true, GLFW.GLFW_KEY_P);
	
	public HUDEditorModule() {
		super("HUDEditor","Module containing HUDEditor settings.",()->true,false);
		this.addSettings(showHUD, keybind);	}
}
