/*
 * Copyright (c) 2014-2023 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.nebula.utils.altmanager.screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import dev.nebula.utils.altmanager.Alt;
import dev.nebula.utils.altmanager.AltManager;
import dev.nebula.utils.altmanager.MojangAlt;

public final class EditAltScreen extends AltEditorScreen
{
	private final AltManager altManager;
	private Alt editedAlt;
	
	public EditAltScreen(Screen prevScreen, AltManager altManager,
		Alt editedAlt)
	{
		super(prevScreen, Text.literal("Edit Alt"));
		this.altManager = altManager;
		this.editedAlt = editedAlt;
	}
	
	@Override
	protected String getDefaultNameOrEmail()
	{
		return editedAlt instanceof MojangAlt
			? ((MojangAlt)editedAlt).getEmail() : editedAlt.getName();
	}
	
	@Override
	protected String getDefaultPassword()
	{
		return editedAlt instanceof MojangAlt
			? ((MojangAlt)editedAlt).getPassword() : "";
	}
	
	@Override
	protected String getDoneButtonText()
	{
		return "Save";
	}
	
	@Override
	protected void pressDoneButton()
	{
		altManager.edit(editedAlt, getNameOrEmail(), getPassword());
		client.setScreen(prevScreen);
	}
}
