package dev.vili.haiku.module.modules.player;

import dev.vili.haiku.module.Module;
import org.lwjgl.glfw.GLFW;

public final class QuickDisconnect extends Module
{
	public QuickDisconnect()
	{
		super("QuickDisconnect", "Use a hotkey to immediately leave the server", GLFW.GLFW_KEY_UNKNOWN, Category.PLAYER);
	}

	@Override
	public void onEnable()
	{
		mc.world.disconnect();
		super.onEnable();
	}
	
	@Override
	public void onDisable()
	{
		super.onDisable();
	}
}