/*
 * Copyright (C) 2013 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sound;

import java.util.logging.Logger;

/**
 *
 * @author jkeller1
 */
public class SoundException extends Exception {

    /**
     *
     */
    public SoundException() {
    }

    /**
     *
     * @param s
     */
    public SoundException(String s) {
        super("Sound exception: " + s);
    }
    private static final Logger LOG = Logger.getLogger(SoundException.class.getName());
}
