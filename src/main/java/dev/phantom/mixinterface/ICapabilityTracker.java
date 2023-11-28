/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.phantom.mixinterface;

public interface ICapabilityTracker {
    boolean get();

    void set(boolean state);
}
