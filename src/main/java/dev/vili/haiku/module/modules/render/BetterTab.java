/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.module.modules.render;

import dev.vili.haiku.module.Module;
import dev.vili.haiku.setting.settings.BooleanSetting;
import dev.vili.haiku.setting.settings.ColorSetting;
import dev.vili.haiku.setting.settings.NumberSetting;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;

import org.lwjgl.glfw.GLFW;

public class BetterTab extends Module 
{

    public final BooleanSetting tabSize = new BooleanSetting("tablistSize-BetterTab", "How many players in total to display in the tablist.", true);
    public final NumberSetting tabHeight = new NumberSetting("columnHeight-BetterTab", "How many players to display in each column.", 20, 1, 1000, 1);
    public final BooleanSetting self = new BooleanSetting("highlightSelf-BetterTab", "Highlights yourself in the tablist.", true);
    public final ColorSetting selfColor = new ColorSetting("selfColor-BetterTab", "The color to highlight your name with.");
    public final BooleanSetting friends = new BooleanSetting("highlightFriends-BetterTab", "Highlights friends in the tablist.", true);
    public final BooleanSetting accurateLatency = new BooleanSetting("accurateLatency-BetterTab", "Shows latency as a number in the tablist.", true);
    public final BooleanSetting gamemode = new BooleanSetting("gamemode-BetterTab", "Display gamemode next to the nick.", false);
    public BetterTab() {
        super("betterTab", "Various improvements to the tab list.", GLFW.GLFW_KEY_UNKNOWN, Category.RENDER);
        this.addSettings(tabSize, tabHeight, self, selfColor, friends, accurateLatency, gamemode);
    }

    public Text getPlayerName(PlayerListEntry playerListEntry) {
        Text name;
        float[] color = null;

        name = playerListEntry.getDisplayName();
        if (name == null) name = Text.literal(playerListEntry.getProfile().getName());

        if (playerListEntry.getProfile().getId().toString().equals(mc.player.getGameProfile().getId().toString()) && self.isEnabled()) {
            color = selfColor.color();
        }
        // else if (friends.get() && Friends.get().isFriend(playerListEntry)) {
        //     Friend friend = Friends.get().get(playerListEntry);
        //     if (friend != null) color = Config.get().friendColor.get();
        // }

        if (color != null) {
            String nameString = name.getString();

            for (Formatting format : Formatting.values()) {
                if (format.isColor()) nameString = nameString.replace(format.toString(), "");
            }

            name = Text.literal(nameString).setStyle(name.getStyle().withColor(TextColor.fromRgb((int) (color[0] * 255) << 16 | (int) (color[1] * 255) << 8 | (int) (color[2] * 255))));
        }

        if (gamemode.isEnabled()) {
            GameMode gm = playerListEntry.getGameMode();
            String gmText = "?";
            if (gm != null) {
                gmText = switch (gm) {
                    case SPECTATOR -> "Sp";
                    case SURVIVAL -> "S";
                    case CREATIVE -> "C";
                    case ADVENTURE -> "A";
                };
            }
            MutableText text = Text.literal("");
            text.append(name);
            text.append(" [" + gmText + "]");
            name = text;
        }

        return name;
    }

}
