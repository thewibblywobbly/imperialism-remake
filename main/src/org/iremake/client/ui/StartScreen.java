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
package org.iremake.client.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import org.iremake.client.Option;
import org.iremake.client.StartClient;
import org.iremake.client.resources.IOManager;
import org.iremake.client.resources.Places;
import org.iremake.client.ui.editor.EditorScreen;
import org.tools.ui.BrowserPanel;
import org.tools.ui.ButtonBar;
import org.tools.ui.layout.RelativeLayout;
import org.tools.ui.layout.RelativeLayoutConstraint;
import org.tools.ui.utils.WindowCorner;

/**
 *
 */
public class StartScreen extends UIFrame {

    public StartScreen() {

        JLayeredPane pane = new JLayeredPane();

        // create menu bar and add to frame
        JComponent menuBar = createMenuBar();

        // language drop down menu
        JComboBox<String> languageComboBox = new JComboBox<>();
        languageComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"English", "Spanish", "German", "Chinese"}));   // set model
        languageComboBox.setToolTipText("Select language");     // tooltip

        // background image
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setIcon(IOManager.getAsIcon(Places.GraphicsIcons, "start.background.png"));    // set image

        // logo label
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(IOManager.getAsIcon(Places.GraphicsIcons, "start.logo.png"));    // set image

        // Version label
        JLabel versionLabel = new JLabel("Version " + Option.Version.get());
        versionLabel.setForeground(Color.WHITE);    // white color

        RelativeLayout layout = new RelativeLayout();
        pane.setLayout(layout);

        // add background image (centered)
        pane.add(backgroundLabel, new Integer(1));
        layout.addConstraint(backgroundLabel, RelativeLayoutConstraint.centered());

        // add logo (horizontally centerd, vertically at 40%)
        pane.add(logoLabel, new Integer(2));
        layout.addConstraint(logoLabel, RelativeLayoutConstraint.relative(0.5f, 0.4f));

        // add Version in the same layer (right, lower border)
        pane.add(versionLabel, new Integer(2));
        layout.addConstraint(versionLabel, RelativeLayoutConstraint.corner(WindowCorner.SouthWest, 20, 20));

        // add menubar on top and position
        pane.add(menuBar, new Integer(3));
        layout.addConstraint(menuBar, RelativeLayoutConstraint.relative(0.5f, 0.6f));

        // add language combo box
        pane.add(languageComboBox, new Integer(4));
        layout.addConstraint(languageComboBox, RelativeLayoutConstraint.relative(0.7f, 0.5f));

        pane.setOpaque(true);
        pane.setBackground(Color.black);
        // set background of content pane (which is also included in layered pane)
        // frame.getContentPane().setBackground(Color.BLACK);  // black background

        setContent(pane);
    }



    /**
     * Makes the menu bar.
     *
     * @param owner
     * @return
     */
    private JComponent createMenuBar() {
        // scenario button
        JButton scenarioButton = Button.StartMenuScenario.create();
        scenarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIDialog dialog = new NewLocalScenarioDialog();
                dialog.start();
            }
        });

        // network button
        JButton networkButton = Button.StartMenuNetwork.create();
        networkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIDialog dialog = new NetworkDialog();
                dialog.start();
            }
        });

        // options button
        JButton optionsButton = Button.StartMenuOptions.create();
        optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OptionsDialog dialog = new OptionsDialog();
                dialog.start();
            }
        });

        // help button
        JButton helpButton = Button.StartMenuHelp.create();
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                URL index = IOManager.getURL(Places.Help, "en_index.html");
                BrowserPanel browser = new BrowserPanel(index, index, IOManager.getAsLoader(Places.GraphicsIcons));
                UIDialog.make(browser, "Help");
            }
        });

        // editor button
        JButton editorButton = Button.StartMenuEditor.create();
        editorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIFrame frame = new EditorScreen();
                frame.switchTo();
            }
        });

        // exit button
        JButton exitButton = Button.StartMenuExit.create();
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StartClient.shutDown();
            }
        });

        ButtonBar bar = new ButtonBar();
        bar.add(scenarioButton, networkButton, optionsButton, helpButton, editorButton, exitButton);

        return bar.get();
    }
}