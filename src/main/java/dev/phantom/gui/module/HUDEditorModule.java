package dev.phantom.gui.module;

import org.lwjgl.glfw.GLFW;

import dev.phantom.gui.setting.BooleanSetting;
import dev.phantom.gui.setting.KeybindSetting;

public class HUDEditorModule extends Module {
	public static final BooleanSetting showHUD=new BooleanSetting("Show HUD Panels","showHUD","Whether to show the HUD panels in the ClickGUI.",()->true,true);
	public static final KeybindSetting keybind=new KeybindSetting("Keybind","keybind","The key to toggle the module.",()->true,GLFW.GLFW_KEY_P);
	
	public HUDEditorModule() {
		super("HUDEditor","Module containing HUDEditor settings.",()->true,false);
		settings.add(showHUD);
		settings.add(keybind);
	}
}
