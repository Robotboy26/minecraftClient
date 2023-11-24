/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.phantom.event;

import dev.phantom.eventbus.PhantomEvent;
import net.minecraft.client.MinecraftClient;

public class Event extends PhantomEvent {
    public MinecraftClient mc = MinecraftClient.getInstance();

    public Event() {
    }
}
