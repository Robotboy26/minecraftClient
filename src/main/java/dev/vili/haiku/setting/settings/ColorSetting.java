/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.vili.haiku.setting.settings;

import dev.vili.haiku.setting.Setting;

public class ColorSetting extends Setting {
    public float red;
    public float green;
    public float blue;

    public ColorSetting(String name, String description, float red, float green, float blue) {
        super(name, description);
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Gets the enabled state of the setting.
     */
    public float[] color() {
        float[] rgb = new float[3];
        rgb[0] = this.red;
        rgb[1] = this.green;
        rgb[2] = this.blue;
        return rgb;
    }

    public float getRed() {
        return this.red;
    }

    public float getGreen() {
        return this.green;
    }

    public float getBlue() {
        return this.blue;
    }

    /**
     * Sets the enabled state of the setting.
     *
     * @param color enabled state to set
     */
    public void setColor(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int[] getRGBArray() {
        int[] rgb = new int[3];
        rgb[0] = (int) (this.red * 255);
        rgb[1] = (int) (this.green * 255);
        rgb[2] = (int) (this.blue * 255);
        return rgb;
    }
}
