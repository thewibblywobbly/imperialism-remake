/*
 * Copyright (C) 2012 Trilarion
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
package org.iremake.client.ui.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.iremake.client.ui.Button;
import org.iremake.client.ui.model.UIScenario;
import org.iremake.common.Settings;
import org.iremake.common.model.Nation;
import org.iremake.common.model.map.MapPosition;

/**
 * Mini map panel. Provides overview maps and can change the view of the main
 * map.
 */
public class MiniMapPanel extends JPanel {

    private static final Logger LOG = Logger.getLogger(MiniMapPanel.class.getName());
    private enum Type {

        Geographical, Political;
    }
    private static final long serialVersionUID = 1L;
    private Dimension size = new Dimension();
    private Rectangle focus = new Rectangle();
    private MiniMapFocusChangedListener focusChangedListener;
    /* client scenario */
    private UIScenario scenario;
    /* the image is stored so it doesn't need to be calculated for every repaint */
    private BufferedImage buffer;
    private int headerHeight = 0;
    private Type type = Type.Geographical;

    /**
     * Feed it an scenario.
     *
     * @param scenario the scenario
     */
    public MiniMapPanel(UIScenario scenario) {

        this.scenario = scenario;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && focus.width > 0) {
                    int x0 = e.getX();
                    int y0 = e.getY() - headerHeight;
                    x0 = Math.min(Math.max(x0, focus.width / 2), size.width - focus.width / 2);
                    y0 = Math.min(Math.max(y0, focus.height / 2), size.height - focus.height / 2);
                    if (focus.x != x0 || focus.y != y0) {
                        focus.x = x0;
                        focus.y = y0;
                        MiniMapPanel.this.repaint();
                        notifyFocusChangedListener();
                    }
                }
            }
        });

        JButton geoButton = Button.MiniMapGeographical.create();
        geoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchTo(Type.Geographical);
            }
        });
        JButton polButton = Button.MiniMapPolitical.create();
        polButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchTo(Type.Political);
            }
        });
        // ButtonBar bar = new ButtonBar(geoButton, polButton);

        headerHeight = geoButton.getPreferredSize().height;

        setLayout(new MigLayout("", "0[]", "0[]"));
        add(geoButton);
        add(polButton);

        setBorder(BorderFactory.createLineBorder(Color.black, 1));
        setOpaque(true); // by default is not
    }

    /**
     * Pain the mini map.
     *
     * @param g graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        Rectangle bounds = getBounds();
        g2d.setColor(Color.lightGray);
        g2d.fillRect(0, 0, bounds.width, headerHeight);

        // draw background
        if (buffer != null) {
            g2d.drawImage(buffer, 0, headerHeight, this);
        }

        // the main map has told us their size, we can draw the focus rectangle
        if (focus.width > 0) {
            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.black);
            g2d.drawRect(focus.x - focus.width / 2 + 1, focus.y - focus.height / 2 + 1 + headerHeight, focus.width - 2, focus.height - 2);
            g2d.setStroke(oldStroke);
        }
    }

    /**
     * tell the listener that we have a new focus
     */
    private void notifyFocusChangedListener() {
        if (focusChangedListener != null && focus.width > 0) {
            focusChangedListener.miniMapFocusChanged((float) focus.x / size.width, (float) focus.y / size.height);
        }
    }

    /**
     * sets the focus changed listener
     *
     * @param l the listener
     */
    public void setFocusChangedListener(MiniMapFocusChangedListener l) {
        focusChangedListener = l;
    }

    /**
     * somebody tells us what the correct size for the view rectangle is in
     * normalized [0,1] coordinates.
     *
     * @param fractionRows fraction of rows that fit into the main map
     * @param fractionColumns fraction of columns that fit into the main map
     */
    public void mapChanged(float fractionRows, float fractionColumns) {

        int width = getSize().width;
        size = new Dimension(width, width * scenario.getNumberRows() / scenario.getNumberColumns());
        setPreferredSize(new Dimension(size.width, size.height + headerHeight));

        focus.x = size.width / 2;
        focus.y = size.height / 2;

        focus.width = (int) (size.width * fractionColumns);
        focus.height = (int) (size.height * fractionRows);

        notifyFocusChangedListener();

        redrawMap(); // TODO wait for the actual componenResized event
        revalidate(); // this will call repaint
    }

    /**
     * somebody tells us that a tile changed
     */
    public void tileChanged() {
        // TODO is there a more clever way then redrawing everything?
        redrawMap();
        repaint();
    }

    private void switchTo(Type type) {
        if (type != this.type) {
            this.type = type;
            redrawMap();
            repaint();
        }
    }

    /**
     * Put new map content in buffer image.
     */
    private void redrawMap() {
        switch (type) {
            case Geographical:
                buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < size.width; x++) {
                    for (int y = 0; y < size.height; y++) {
                        int column = scenario.getNumberColumns() * x / size.width; // rounding down
                        int row = scenario.getNumberRows() * y / size.height;
                        Color color = scenario.getTerrainTileColorAt(new MapPosition(row, column));
                        buffer.setRGB(x, y, color.getRGB());
                    }
                }
                break;

            case Political:
                buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < size.width; x++) {
                    for (int y = 0; y < size.height; y++) {
                        int column = scenario.getNumberColumns() * x / size.width; // rounding down
                        int row = scenario.getNumberRows() * y / size.height;
                        Nation nation = scenario.getNationAt(new MapPosition(row, column));
                        if (nation != null) {
                            Color color = nation.getColor();
                            buffer.setRGB(x, y, color.getRGB());
                        } else {
                            // TODO ocean color?
                            buffer.setRGB(x, y, scenario.getTileGraphicsRepository().getTerrainTileColor(Settings.getDefaultTerrainID()).getRGB());
                        }
                    }
                }
                break;
        }

    }
}
