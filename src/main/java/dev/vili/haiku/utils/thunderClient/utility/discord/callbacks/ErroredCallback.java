package dev.vili.haiku.utils.thunderClient.utility.discord.callbacks;

import com.sun.jna.Callback;

public interface ErroredCallback extends Callback {
    void apply(final int p0, final String p1);
}
