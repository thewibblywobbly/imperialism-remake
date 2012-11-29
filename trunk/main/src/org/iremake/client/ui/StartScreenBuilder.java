/*
 * Copyright (C) 2012 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either Version 3 of the License, or
 * (at your option) any later Version.
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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JToolBar;
import org.iremake.client.Options;
import org.iremake.client.StartClient;
import org.iremake.client.resources.IOManager;
import org.iremake.client.resources.Places;
import org.iremake.common.ui.BrowserPanel;
import org.iremake.common.ui.layout.RelativeLayout;
import org.iremake.common.ui.layout.RelativeLayoutConstraint;
import org.iremake.common.ui.utils.GraphicsUtils;
import org.iremake.common.ui.utils.WindowCorner;

/**
 * Initial building of the start screen.
 *
 */
// TODO Dialogs and Frames are disposed now when they aren't needed, maybe for performance reason they should be re-used
// TODO Frames change between editor, start and main, should we work with rootpanes instead?
// TODO Special open source Font throughout the game (?)
// TODO background of dialogs is not white (panel in contentpane?)
public class StartScreenBuilder {

    /**
     * No instantiation.
     */
    private StartScreenBuilder() {
    }

    /**
     * Makes the start frame.
     *
     * @return
     */
    public static JComponent build() {

        JLayeredPane pane = new JLayeredPane();

        // create menu bar and add to frame
        JToolBar menuBar = StartScreenBuilder.menuBar();

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
        JLabel versionLabel = new JLabel("Version " + Options.Version.get());
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

        return pane;
    }

    /**
     * Makes the menu bar.
     *
     * @param owner
     * @return
     */
    private static JToolBar menuBar() {
        // tool bar
        JToolBar bar = GraphicsUtils.makeToolBar(false, false);

        // scenario button
        JButton scenarioButton = Button.StartMenuScenario.create();
        scenarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScenarioDialogsBuilder.makeLoadDialog();
            }
        });

        // network button
        JButton networkButton = Button.StartMenuNetwork.create();
        networkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NetworkDialogBuilder.makeDialog();
            }
        });

        // options button
        JButton optionsButton = Button.StartMenuOptions.create();
        optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OptionsDialogBuilder.makeDialog();
            }
        });

        // help button
        JButton helpButton = Button.StartMenuHelp.create();
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                URL index = IOManager.getURL(Places.Help, "en_index.html");
                JDialog dialog = FrameManager.getInstance().makeDialog("Help", new Dimension(800, 700));
                BrowserPanel browser = new BrowserPanel(index, index, IOManager.getAsLoader(Places.GraphicsIcons));
                dialog.add(browser);
                dialog.setVisible(true);
            }
        });

        // editor button
        JButton editorButton = Button.StartMenuEditor.create();
        editorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameManager.getInstance().switchToEditorScreen();
                // EditorBuilder.build();
                // TODO first build editor, then dispose start frame, then build editor visible, then load default scenario
            }
        });

        // exit button
        JButton exitButton = Button.StartMenuExit.create();
        exitButton.addActionListener(new ActionListener() {    // add action listener
            @Override
            public void actionPerformed(ActionEvent e) {
                StartClient.shutDown();
            }
        });

        // add buttons to toolbar
        bar.add(scenarioButton);
        bar.add(networkButton);
        bar.add(optionsButton);
        bar.add(helpButton);
        bar.add(editorButton);
        bar.add(exitButton);

        return bar;
    }
}