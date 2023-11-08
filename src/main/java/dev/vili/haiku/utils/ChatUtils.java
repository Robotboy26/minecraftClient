/*
 * Copyright (c) 2014-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.utils;

import java.util.List;
import java.util.StringJoiner;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
//import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.MutableText;
//import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import dev.vili.haiku.Haiku;

public enum ChatUtils
{
	;
	
	private static final MinecraftClient MC = Haiku.mc;
	
	public static final String WURST_PREFIX =
		"";
	private static final String WARNING_PREFIX =
		"";
	private static final String ERROR_PREFIX =
		"";
	private static final String SYNTAX_ERROR_PREFIX =
		"";
	
	private static boolean enabled = true;
	
	public static void setEnabled(boolean enabled)
	{
		ChatUtils.enabled = enabled;
	}
	
	public static void component(Text component)
	{
		if(!enabled)
			return;
		
		ChatHud chatHud = MC.inGameHud.getChatHud();
		MutableText prefix = Text.literal(WURST_PREFIX);
		chatHud.addMessage(prefix.append(component));
	}
	
	public static void message(String message)
	{
		component(Text.literal(message));
	}
	
	public static void warning(String message)
	{
		message(WARNING_PREFIX + message);
	}
	
	public static void error(String message)
	{
		message(ERROR_PREFIX + message);
	}
	
	public static void syntaxError(String message)
	{
		message(SYNTAX_ERROR_PREFIX + message);
	}
	
	// public static String getAsString(ChatHudLine.Visible visible)
	// {
	// 	return getAsString(visible.content());
	// }
	
	// public static String getAsString(OrderedText text)
	// {
	// 	JustGiveMeTheStringVisitor visitor = new JustGiveMeTheStringVisitor();
	// 	text.accept(visitor);
	// 	return visitor.toString();
	// }
	
	public static final String wrapText(String text, int width)
	{
		return wrapText(text, width, Style.EMPTY);
	}
	
	public static final String wrapText(String text, int width, Style style)
	{
		List<StringVisitable> lines = MC.textRenderer.getTextHandler()
			.wrapLines(text, width, Style.EMPTY);
		
		StringJoiner joiner = new StringJoiner("\n");
		lines.stream().map(StringVisitable::getString)
			.forEach(s -> joiner.add(s));
		
		return joiner.toString();
	}
}
