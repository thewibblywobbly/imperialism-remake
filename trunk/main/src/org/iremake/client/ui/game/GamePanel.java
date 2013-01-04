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
package org.iremake.client.ui.game;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.iremake.client.ui.Button;

/**
 *
 */
public enum GamePanel {

    Industry("industry"),
    Diplomacy("diplomacy"),
    Military("military"),
    Research("research"),
    Statistics("statistics"),
    Trade("trade"),
    Transport("transport");
    private final static Logger LOG = Logger.getLogger(GamePanel.class.getName());
    private String title;

    GamePanel(String title) {
        this.title = title;
    }

    /**
     * Just instantiating the right class which is the name of the enum plus
     * "Panel".
     *
     * @return
     */
    // TODO do we want it that way?
    public JPanel create() {
        JPanel panel = null;
        try {
            panel = (JPanel) Class.forName(GamePanel.class.getPackage().getName() + "." + this.name() + "Panel").newInstance();
            panel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return panel;
    }

    public JButton getButton() {
        return Button.create("game/game.button." + title + ".png");
    }
}
