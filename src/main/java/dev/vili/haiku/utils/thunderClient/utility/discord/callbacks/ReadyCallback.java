package dev.vili.haiku.utils.thunderClient.utility.discord.callbacks;

import dev.vili.haiku.utils.thunderClient.utility.discord.DiscordUser;
import com.sun.jna.Callback;

public interface ReadyCallback extends Callback {
    void apply(final DiscordUser p0);
}