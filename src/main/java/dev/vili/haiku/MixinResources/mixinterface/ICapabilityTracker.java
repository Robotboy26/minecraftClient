/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.vili.haiku.MixinResources.mixinterface;

public interface ICapabilityTracker {
    boolean get();

    void set(boolean state);
}
