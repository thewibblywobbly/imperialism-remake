/*
 * Copyright (C) 2013 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.others.swing;

import java.awt.GraphicsDevice;
import java.awt.GraphicsDevice.WindowTranslucency;
import java.awt.GraphicsEnvironment;
import java.util.logging.Logger;

/**
 * Test of windows translucency capabilities of Java 7.
 *
 * The uniform opacity is simply set by setOpacity(float) on the window.
 *
 * See also: http://docs.oracle.com/javase/tutorial/uiswing/misc/trans_shaped_windows.html
 */
public class TranslucentWindows {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Determine if the GraphicsDevice supports translucency
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        System.out.println("Supported uniform window   translucency: " + gd.isWindowTranslucencySupported(WindowTranslucency.TRANSLUCENT));
        System.out.println("Supported per pixel window transparency: " + gd.isWindowTranslucencySupported(WindowTranslucency.PERPIXEL_TRANSPARENT));
        System.out.println("Supported per pixel window translucency: " + gd.isWindowTranslucencySupported(WindowTranslucency.PERPIXEL_TRANSLUCENT));
    }
    private static final Logger LOG = Logger.getLogger(TranslucentWindows.class.getName());
}
