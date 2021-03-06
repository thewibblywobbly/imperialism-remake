/*
 * Copyright (C) 2011 Trilarion
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
package org.tools.ui.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * Collection of static helper routines.
 */
public class GraphicsUtils {

    private static final Logger LOG = Logger.getLogger(GraphicsUtils.class.getName());
    /**
     *
     */
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    /**
     *
     */
    private static final String KEYID = "scrambledEggs";
    private final static Rectangle ScreenBounds;
    /**
     *
     */
    public final static Rectangle MaximumBounds;

    /* We determine the screen bounds once at the start of the programm in this static initialization section */
    static {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = env.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        ScreenBounds = gc.getBounds();

        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
        MaximumBounds = new Rectangle(ScreenBounds.x + insets.left, ScreenBounds.y + insets.top,
                ScreenBounds.width - insets.left - insets.right, ScreenBounds.height - insets.top - insets.bottom);
    }

    /**
     * Private constructor to avoid instantiation.
     */
    private GraphicsUtils() {
    }

    /**
     *
     * @param window
     * @param corner
     */
    public static void setLocationRelativeToDesktop(Window window, WindowCorner corner) {

        // get screen size
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        // get screem insets
        GraphicsConfiguration gc = window.getGraphicsConfiguration();
        Insets insets = toolkit.getScreenInsets(gc);

        // construct bounds for screen minus insets
        Rectangle desktopBounds = new Rectangle(insets.left, insets.top, screenSize.width - insets.left - insets.right, screenSize.height - insets.top - insets.bottom);

        // set location
        setLocation(window, window.getBounds(), desktopBounds, corner);
    }

    /**
     *
     * @param component
     * @param frame
     * @param corner
     */
    public static void setLocationRelativeTo(Component component, Component frame, WindowCorner corner) {
        setLocation(component, component.getBounds(), frame.getBounds(), corner);
    }

    /**
     *
     * @param A
     * @param boundsA
     * @param boundsB
     * @param corner
     */
    private static void setLocation(Component A, Rectangle boundsA, Rectangle boundsB, WindowCorner corner) {
        switch (corner) {
            case NorthWest:
                boundsA.x = boundsB.x;
                boundsA.y = boundsB.y;
                break;
            case NorthEast:
                boundsA.x = boundsB.x + boundsB.width - boundsA.width;
                boundsA.y = boundsB.y;
                break;
            case SouthWest:
                boundsA.x = boundsB.x;
                boundsA.y = boundsB.y + boundsB.height - boundsA.height;
                break;
            case SouthEast:
                boundsA.x = boundsB.x + boundsB.width - boundsA.width;
                boundsA.y = boundsB.y + boundsB.height - boundsA.height;
                break;
        }

        A.setBounds(boundsA);
    }

    /**
     *
     * @param icon
     * @param width
     * @param height
     * @return
     */
    public static ImageIcon scaleIconTo(ImageIcon icon, int width, int height) {
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        icon.setImage(image);
        return icon;
    }

    /**
     *
     * @param listener
     * @return
     */
    public static Action actionWrapper(final ActionListener listener) {
        return new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.actionPerformed(e);
                }
            }
        };
    }

    /**
     *
     * @param component
     * @param keyStroke
     * @param action
     */
    public static void setKeyStroke(JComponent component, KeyStroke keyStroke, Action action) {
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        if (inputMap != null) {
            ActionMap actionMap = component.getActionMap();
            inputMap.put(keyStroke, KEYID);
            if (actionMap != null) {
                actionMap.put(KEYID, action);
            }
        }
    }

    /**
     *
     * @param button
     * @param keyStroke
     */
    public static void setButtonKeyStroke(final JButton button, KeyStroke keyStroke) {
        Action action = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (button != null) {
                    button.doClick();
                }
            }
        };
        setKeyStroke(button, keyStroke, action);
    }

    /**
     *
     * @param component
     * @return
     */
    public static KeyStroke getKeyStroke(JComponent component) {
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        if (inputMap != null) {
            KeyStroke[] keyStrokes = inputMap.keys();
            if (keyStrokes != null) {
                for (KeyStroke keyStroke : keyStrokes) {
                    if (KEYID.equals(inputMap.get(keyStroke))) {
                        return keyStroke;
                    }
                }
            }
        }
        return null;
    }

    /**
     *
     * @param component
     */
    public static void clearKeyStroke(JComponent component) {
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        if (inputMap != null) {
            KeyStroke[] keyStrokes = inputMap.keys();
            if (keyStrokes != null) {
                for (KeyStroke keyStroke : inputMap.keys()) {
                    if (KEYID.equals(inputMap.get(keyStroke))) {
                        inputMap.remove(keyStroke);
                        break;
                    }
                }
            }
        }
        ActionMap actionMap = component.getActionMap();
        if (actionMap != null) {
            actionMap.remove(KEYID);
        }
    }

    /**
     * Get the bounds of the parent of the dragged component.
     *
     * @param source
     * @return
     */
    // TODO most certainly they want the bounds, not the dimension because it mustn't start at 0,0
    public static Dimension getBoundingSize(Component source) {
        if (source instanceof Window) {
            return new Dimension(MaximumBounds.width, MaximumBounds.height);
        } else {
            return source.getParent().getSize();
        }
    }

    /**
     *
     * @param frame
     */
    public static void setFullScreen(Frame frame) {
        frame.setBounds(ScreenBounds);
    }

    /**
     *
     * @param frame
     */
    public static void setToMaximumBounds(Frame frame) {
        frame.setBounds(MaximumBounds);
    }

    /**
     * Given two sides (at most one can be Null) returns the appropriate type of
     * cursor for resizing.
     *
     * @param vertical North or South
     * @param horizontal East or West
     * @return
     */
    public static int getResizeCursorForSide(WindowSide vertical, WindowSide horizontal) {
        int type = -1;
        switch (vertical) {
            case North:
                switch (horizontal) {
                    case East:
                        // N+E
                        type = Cursor.NE_RESIZE_CURSOR;
                        break;
                    case West:
                        // N+W
                        type = Cursor.NW_RESIZE_CURSOR;
                        break;
                    case None:
                        // N alone
                        type = Cursor.N_RESIZE_CURSOR;
                        break;
                }
                break;
            case South:
                switch (horizontal) {
                    case East:
                        // S+E
                        type = Cursor.SE_RESIZE_CURSOR;
                        break;
                    case West:
                        // S+W
                        type = Cursor.SW_RESIZE_CURSOR;
                        break;
                    case None:
                        // S alone
                        type = Cursor.S_RESIZE_CURSOR;
                        break;
                }
                break;
            case None:
                // assume that neither North nor South, most probably Null
                switch (horizontal) {
                    case East:
                        // E alone
                        type = Cursor.E_RESIZE_CURSOR;
                        break;
                    case West:
                        // W alone
                        type = Cursor.W_RESIZE_CURSOR;
                        break;
                }
                break;
        }
        return type;
    }

    /**
     * Parses a six character string (hex presentation) into a Color object.
     *
     * @param hex a 6 character string as "ffffff" for white
     * @return the corresponding color
     */
    public static Color convertHexToColor(String hex) {
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return new Color(r, g, b);
    }

    /**
     * Deep copy of a BufferedImage excluding the properties.
     *
     * @param in Input BufferedImage
     * @return Output BufferedImage
     */
    public static BufferedImage copyBufferedImage(BufferedImage in) {
        ColorModel cm = in.getColorModel();
        WritableRaster wr = in.copyData(null);
        return new BufferedImage(cm, wr, cm.isAlphaPremultiplied(), null);
    }

    /**
     * Scales an Image.
     *
     * @param original Original image.
     * @param dims New dimensions.
     * @return The scaled image of type 4Byte_ABGR
     */
    public static BufferedImage scaleBufferedImage(Image original, Dimension dims) {
        // create new empty image with right size
        BufferedImage scaled = new BufferedImage(dims.width, dims.height, BufferedImage.TYPE_4BYTE_ABGR);

        // get graphics context and set rendering hints to interpolation
        Graphics2D g2d = scaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // draw original into new image
        g2d.drawImage(original, 0, 0, dims.width, dims.height, null);
        g2d.dispose();

        return scaled;
    }
    
    /**
     * Copies part of an image.
     * 
     * @param original
     * @param area
     * @return 
     */
    public static BufferedImage subBufferedImage(Image original, Rectangle area) {
        BufferedImage cutout = new BufferedImage(area.width, area.height, BufferedImage.TYPE_4BYTE_ABGR);
        
        Graphics2D g2d = cutout.createGraphics();
        g2d.drawImage(original, -area.x, -area.y, null);
        
        return cutout;
    }
    
    /**
     * 
     * @param image
     * @param xi
     * @param yi 
     */
    public static void transparentFillPolygon(BufferedImage image, int[] xi, int[] yi) {
        if (xi == null || yi == null || xi.length != yi.length) {
            throw new RuntimeException("xi or yi are null or they have not the same length!");
        }
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(TRANSPARENT);
        g2d.setComposite(AlphaComposite.Src);
        g2d.fillPolygon(xi, yi, xi.length);
    }
}
