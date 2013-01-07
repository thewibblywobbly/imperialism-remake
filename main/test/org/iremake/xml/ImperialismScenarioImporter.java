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
package org.iremake.xml;

import icons.TestIOManager;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import org.iremake.common.model.MapPosition;
import org.iremake.common.model.Nation;
import org.iremake.common.model.Province;
import org.iremake.common.model.Scenario;
import org.iremake.common.model.Tile;
import org.tools.io.FileResource;
import org.tools.io.Resource;
import org.tools.ui.utils.LookAndFeel;
import org.tools.xml.XList;
import org.tools.xml.XMLHelper;

/**
 * Reads the output from the python map import script and inserts a scenario
 * file accordingly.
 */
public class ImperialismScenarioImporter extends JFrame {

    private static final long serialVersionUID = 1L;
    private JFileChooser fileChooser;

    /**
     * Creates new form ImperialismScenarioImporter
     */
    public ImperialismScenarioImporter() {
        // form initialization
        initComponents();

        // icon
        setIconImage(TestIOManager.getAsImage("/icons/app.icon.png"));

        // init file chooser
        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || (f.getName().endsWith(".map") || f.getName().endsWith(".xml"));
            }

            @Override
            public String getDescription() {
                return "Map or Scenario Files";
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        importmapTextField = new JTextField();
        chooseImportmapButton = new JButton();
        scenarioTextField = new JTextField();
        chooseScenarioButton = new JButton();
        importButton = new JButton();
        progressBar = new JProgressBar();
        statusScrollPane = new JScrollPane();
        statusTextArea = new JTextArea();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Imperialism Map Import");
        setLocationByPlatform(true);
        setResizable(false);

        importmapTextField.setText("C:\\40_Programmieren\\02_Java Projects\\Imperialism Remake\\tools\\s0.imported.map");

        chooseImportmapButton.setText("...");
        chooseImportmapButton.setToolTipText("Choose import map file");
        chooseImportmapButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                chooseImportmapButtonActionPerformed(evt);
            }
        });

        scenarioTextField.setText("C:\\40_Programmieren\\02_Java Projects\\Imperialism Remake\\tools\\scenario.Europe1814.xml");
        scenarioTextField.setToolTipText("Will be modified in the process!");

        chooseScenarioButton.setText("...");
        chooseScenarioButton.setToolTipText("Choose scenario file");
        chooseScenarioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                chooseScenarioButtonActionPerformed(evt);
            }
        });

        importButton.setText("Import Now");
        importButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                importButtonActionPerformed(evt);
            }
        });

        statusTextArea.setColumns(20);
        statusTextArea.setFont(new Font("Tahoma", 0, 11)); // NOI18N
        statusTextArea.setRows(5);
        statusTextArea.setText("status");
        statusScrollPane.setViewportView(statusTextArea);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                            .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                                    .addComponent(scenarioTextField)
                                    .addComponent(importmapTextField))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                    .addComponent(chooseImportmapButton)
                                    .addComponent(chooseScenarioButton, Alignment.TRAILING)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(248, 248, 248)
                        .addComponent(importButton)
                        .addGap(0, 253, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statusScrollPane)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(importmapTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(chooseImportmapButton))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(scenarioTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(chooseScenarioButton))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(statusScrollPane, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(importButton)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chooseImportmapButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_chooseImportmapButtonActionPerformed
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            String name = f.getPath();
            importmapTextField.setText(name);
        }
    }//GEN-LAST:event_chooseImportmapButtonActionPerformed

    private void chooseScenarioButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_chooseScenarioButtonActionPerformed
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            String name = f.getPath();
            scenarioTextField.setText(name);
        }
    }//GEN-LAST:event_chooseScenarioButtonActionPerformed

    private void importButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_importButtonActionPerformed
        progressBar.setValue(0);

        // test files if they exist
        File importFile = new File(importmapTextField.getText());
        if (!importFile.exists() || !importFile.isFile()) {
            updateStatus("import file not found, will stop");
            return;
        }

        File exportFile = new File(scenarioTextField.getText());
        if (!exportFile.exists() || !exportFile.isFile()) {
            updateStatus("export file not found, will stop");
            return;
        }

        // read map import
        ByteBuffer bb;
        try {
            FileInputStream is = new FileInputStream(importFile);
            FileChannel ic = is.getChannel();
            bb = ByteBuffer.allocate((int) ic.size());
            ic.read(bb);
        } catch (IOException ex) {
            updateStatus("could not read import file, will stop");
            return;
        }
        bb.rewind();
        bb.order(ByteOrder.LITTLE_ENDIAN);
        IntBuffer ib = bb.asIntBuffer();
        progressBar.setValue(10);

        // analyze map import
        int columns = ib.get();
        int rows = ib.get();
        if (columns < 0 || columns > 200 || rows < 0 || rows > 200) {
            updateStatus("values for columns/rows out of bounds, will stop");
            return;
        }
        updateStatus(String.format("map size %dx%d", rows, columns));
        // 5 chunks coming
        int size = 6 * columns * rows;
        if (size != ib.remaining()) {
            updateStatus("size of input data not correct, will stop");
            return;
        }

        int chunk = columns * rows;

        int[] terrain_underlay = new int[chunk];
        ib.get(terrain_underlay);

        int[] terrain_overlay = new int[chunk];
        ib.get(terrain_overlay);

        int[] countries = new int[chunk];
        ib.get(countries);

        int[] resources = new int[chunk];
        ib.get(resources);

        int[] provinces = new int[chunk];
        ib.get(provinces);

        int[] cities = new int[chunk];
        ib.get(cities);

        progressBar.setValue(20);
        updateStatus("data imported successfully");

        Scenario scenario = new Scenario();
        scenario.createEmptyMap(rows, columns);

        // check that if terrain_underlay is ocean also terrain_overlay is ocean
        for (int i = 0; i < chunk; i++) {
            if ((terrain_underlay[i] == 5) != (terrain_overlay[i] == 0)) {
                updateStatus("terrain underlay and overlay differ in ocean description, will stop");
                return;
            }
        }

        // detect countries
        Set<Integer> uc = new HashSet<>(20);
        for (int i = 0; i < chunk; i++) {
            if (terrain_underlay[i] != 5) {
                uc.add(countries[i]);
            }
        }
        updateStatus(String.format("contains %d nations", uc.size()));

        // put countries into list
        XList<Nation> nations = scenario.getNations();
        Map<Integer, Nation> nmap = new HashMap<>();
        int id = 1;
        for (Integer i : uc) {
            String name = String.format("Nation %d", id);
            Nation nation = new Nation(name);
            nmap.put(i, nation);
            nations.addElement(nation);
            id++;
        }

        // detect provinces
        Set<Integer> up = new HashSet<>(1000);
        for (int i = 0; i < chunk; i++) {
            if (terrain_underlay[i] != 5) {
                up.add(provinces[i]);
            }
        }
        updateStatus(String.format("contains %d provinces", up.size()));

        // generate province names
        Map<Integer, String> pmap = new HashMap<>();
        id = 1;
        for (Integer i : up) {
            String name = String.format("Province %d", id);
            pmap.put(i, name);
            id++;
        }

        // add provinces to scenario
        Map<Integer, Province> ppmap = new HashMap<>();
        Set<Integer> processed = new HashSet<>(1000);
        for (int i = 0; i < chunk; i++) {
            if (terrain_underlay[i] != 5) {
                if (!processed.contains(provinces[i])) {
                    Nation nation = nmap.get(countries[i]);
                    String name = pmap.get(provinces[i]);
                    Province province = scenario.newProvince(nation, name);
                    ppmap.put(provinces[i], province);
                    processed.add(provinces[i]);
                }
            }
        }

        // set terrain
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                MapPosition pos = new MapPosition(row, column);
                Tile tile = scenario.getTileAt(pos);
                int i = column + row * columns;

                // set terrains
                // sea
                if (terrain_underlay[i] == 5) {
                    tile.terrainID = 1;
                }
                // plains
                if (terrain_underlay[i] == 0 || terrain_underlay[i] == 1 || terrain_underlay[i] == 7) {
                    tile.terrainID = 2;
                }
                // hills
                if (terrain_underlay[i] == 2) {
                    tile.terrainID = 3;
                }
                // mountains
                if (terrain_underlay[i] == 3) {
                    tile.terrainID = 4;
                }
                // tundra
                if (terrain_underlay[i] == 6 && terrain_overlay[i] == 12) {
                    tile.terrainID = 5;
                }
                // swamp
                if (terrain_underlay[i] == 4) {
                    tile.terrainID = 6;
                }
                // desert
                if (terrain_underlay[i] == 6 && terrain_overlay[i] == 11) {
                    tile.terrainID = 7;
                }

                // set resources
                // grain
                tile.resourceVisible = false;
                if (resources[i] == 17) {
                    tile.resourceID = 1;
                    tile.resourceVisible = true;
                }
                // orchard
                if (resources[i] == 18) {
                    tile.resourceID = 2;
                    tile.resourceVisible = true;
                }
                // buffalo
                if (resources[i] == 20) {
                    tile.resourceID = 3;
                    tile.resourceVisible = true;
                }
                // cotton
                if (resources[i] == 0) {
                    tile.resourceID = 4;
                    tile.resourceVisible = true;
                }
                // sheep
                if (resources[i] == 1) {
                    tile.resourceID = 5;
                    tile.resourceVisible = true;
                }
                // forest
                if (resources[i] == 2 && terrain_overlay[i] == 13) {
                    tile.resourceID = 6;
                    tile.resourceVisible = true;
                }
                // scrubforest
                if (resources[i] == 17 && terrain_overlay[i] == 15) {
                    tile.resourceID = 7;
                    tile.resourceVisible = true;
                }
                // oil
                if (resources[i] == 6) {
                    tile.resourceID = 8;
                }
                // coal
                if (resources[i] == 3) {
                    tile.resourceID = 9;
                }
                // ore
                if (resources[i] == 4) {
                    tile.resourceID = 10;
                }

                // set provinces
                if (terrain_underlay[i] != 5) {
                    tile.provinceID = ppmap.get(provinces[i]).getID();
                }

                // if city at this position, tell province about
                if (cities[i] != 0) {
                    ppmap.get(provinces[i]).setTownPosition(pos);
                }
                // if capital, tell nation about
                if (cities[i] == 35) {
                    nmap.get(countries[i]).setCapitalProvince(ppmap.get(provinces[i]).getID());
                }
            }
        }

        try {
            Resource resource = new FileResource(exportFile);
            XMLHelper.write(resource, scenario);
        } catch (IOException ex) {
            updateStatus("could not write to scenario file");
            return;
        }

        updateStatus("conversion successful");
        progressBar.setValue(100);
    }//GEN-LAST:event_importButtonActionPerformed

    private void updateStatus(String message) {
        statusTextArea.setText(statusTextArea.getText() + "\r\n" + message);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        LookAndFeel.setSystemLookAndFeel();

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ImperialismScenarioImporter().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton chooseImportmapButton;
    private JButton chooseScenarioButton;
    private JButton importButton;
    private JTextField importmapTextField;
    private JProgressBar progressBar;
    private JTextField scenarioTextField;
    private JScrollPane statusScrollPane;
    private JTextArea statusTextArea;
    // End of variables declaration//GEN-END:variables
}
