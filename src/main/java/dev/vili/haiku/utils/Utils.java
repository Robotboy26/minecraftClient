/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.utils;

import dev.vili.haiku.mixinterface.IMinecraftClient;
import static dev.vili.haiku.Haiku.mc;

public class Utils {
    public static void rightClick() {
        ((IMinecraftClient) mc).rightClick();
    }
}
