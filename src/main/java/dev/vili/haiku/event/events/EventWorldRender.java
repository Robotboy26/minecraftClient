/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDev/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package dev.vili.haiku.event.events;

import dev.vili.haiku.event.Event;

import net.minecraft.client.util.math.MatrixStack;

public class EventWorldRender extends Event {

	protected float partialTicks;
	protected MatrixStack matrices;
	
	public static class Post extends RenderEvent {
        public Post(float partialTicks, MatrixStack matrixStack) {
            super(partialTicks, matrixStack);
        }
    }

    public static class Pre extends RenderEvent {
        public Pre(float partialTicks, MatrixStack matrixStack) {
            super(partialTicks, matrixStack);
        }
    }

	public float getPartialTicks() {
		return partialTicks;
	}
	
	public MatrixStack getMatrices() {
		return matrices;
	}
}
