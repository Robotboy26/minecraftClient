/*
 * Copyright (c) 2023. Vili and contributors.
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 *  file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */

package dev.nebula.event.events;

import dev.nebula.event.Event;

public class KeyEvent extends Event {
    private final int key;
    private final int code;
    public KeyEvent(int key, int code, Status status) {
        this.key = key;
        this.code = code;
    }

    /**
     * Gets the key.
     *
     * @return key
     */
    public int getKey() {
        return key;
    }

    /**
     * Gets the key code.
     *
     * @return key code
     */
    public int getCode() {
        return code;
    }

    /**
     * Event enums.
     */
    public enum Status {
        PRESSED,
        RELEASED
    }
}
