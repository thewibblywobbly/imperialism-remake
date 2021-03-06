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
package org.iremake.common.model;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iremake.common.Settings;
import org.iremake.common.model.map.MapItem;
import org.iremake.common.model.map.MapPosition;
import org.iremake.common.model.map.Tile;
import org.iremake.common.model.map.TilesBorder;
import org.iremake.common.model.map.TilesTransition;
import org.tools.utils.BitBuffer;
import org.tools.xml.FullXMLable;
import org.tools.xml.Node;
import org.tools.xml.XList;
import org.tools.xml.XProperty;

/**
 * The full internal Scenario model. It can hold all data structures related to
 * a game and can read from a scenario file and store into it again.
 *
 * There should be much more documentation here, but unfortunately so far there
 * isn't.
 */
// TODO this class has not enough methods and therefore exposes local variables
public class ServerScenario implements FullXMLable {

    public static final int BITSIZE_TERRAINID = 5;
    public static final int BITSIZE_RESOURCEID = 5;
    public static final int BITSIZE_PROVINCEID = 14;
    public static final int BITSIZE_RAILROAD_CONFIG = 3;
    public static final int BITSIZE_RIVERID = 6;
    public static final int RIVERID_NONE = 63;
    protected static final String XML_NAME = "Scenario";
    private static final String XML_NAME_NATIONS = "Nations";
    private static final Logger LOG = Logger.getLogger(ServerScenario.class.getName());
    private int rows = 0;
    private int columns = 0;
    private Tile[][] map;
    private XProperty properties = new XProperty(10);
    private XList<Nation> nations = new XList<>(Nation.class, XML_NAME_NATIONS);
    private List<ScenarioChangedListener> listeners = new LinkedList<>();

    /**
     * An empty map consisting of default values for the tile at each position.
     *
     * @param rows number of rows of the new map
     * @param columns number of columns of the new map
     */
    public void createEmptyMap(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            LOG.log(Level.INFO, "Zero or negative sizes!");
            return;
        }
        clear();
        this.rows = rows;
        this.columns = columns;
        map = new Tile[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                map[row][column] = new Tile();
            }
        }
        fireScenarioChanged();
    }

    /**
     * Listeners are not cleared.
     */
    private void clear() {
        properties.clear();
        nations.clear();
        rows = 0;
        columns = 0;
        map = null;
        // TODO notify somebody?
    }

    /**
     *
     * @param p
     * @return
     */
    public boolean containsPosition(MapPosition p) {
        return p.row >= 0 && p.row < rows && p.column >= 0 && p.column < columns;
    }

    /**
     * Internal function allowing for easier syntax and direct use of
     * MapPosition.
     *
     * @param p the map position
     * @return the tile
     */
    private Tile getTile(MapPosition p) {
        return map[p.row][p.column];
    }

    /**
     * Is resource visible.
     *
     * @param p
     * @return
     */
    public boolean isResourceVisibleAt(MapPosition p) {
        if (!containsPosition(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return false;
        }
        return getTile(p).resourceVisible;
    }

    /**
     * Changes the terrain at a specific position.
     *
     * @param p
     * @param id
     */
    public void setTerrainAt(MapPosition p, int id) {
        if (!containsPosition(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return;
        }
        getTile(p).terrainID = id;
        fireTileChanged(p);
    }

    /**
     * Returns the terrain at a specific position.
     *
     * @param p
     * @return
     */
    public Integer getTerrainAt(MapPosition p) {
        if (!containsPosition(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return null;
        }
        return getTile(p).terrainID;
    }

    /**
     *
     * @param p
     * @return
     */
    public int getRiverIDAt(MapPosition p) {
        if (!containsPosition(p)) {
            LOG.log(Level.INFO, "Terrain position outside of map.");
            return -1;
        }
        return getTile(p).riverID;
    }

    /**
     * Convenience function for now. Allows too much access.
     *
     * @param p
     * @return
     */
    public Tile getTileAt(MapPosition p) {
        if (!containsPosition(p)) {
            LOG.log(Level.INFO, "Position outside of map.");
            return null;
        }
        return getTile(p);
    }

    /**
     * @return number of rows of the map
     */
    public int getNumberRows() {
        return rows;
    }

    /**
     * @return number of columns of the map
     */
    public int getNumberColumns() {
        return columns;
    }

    /**
     * @return list of nations, can be used as model for JList
     */
    public XList<Nation> getNations() {
        return nations;
    }

    /**
     * Returns all the provinces of the scenario in an XList, copies the
     * internal list.
     *
     * @return a XList
     */
    public XList<Province> getAllProvinces() {
        XList<Province> list = new XList<>(Province.class);
        for (Nation nation : nations) {
            for (Province province : nation.getProvinces()) {
                list.addElement(province);
            }
        }
        list.sort();
        return list;
    }

    /**
     *
     * @return
     */
    public XList<MapItem> getAllUnits() {
        XList<MapItem> list = new XList<>(MapItem.class);
        for (Nation nation : nations) {
            for (MapItem unit : nation.getUnits()) {
                list.addElement(unit);
            }
        }
        return list;
    }

    /**
     * Returns the nation at a specific position.
     *
     * @param p the position
     * @return the nation
     */
    public Nation getNationAt(MapPosition p) {
        int id = getTile(p).provinceID;
        // need to go through all nations until we find the province, because no map: position -> nation is stored
        for (Nation nation : nations) {
            for (Province province : nation.getProvinces()) {
                if (id == province.getID()) {
                    return nation;
                }
            }
        }
        return null;
    }

    /**
     * Returns the name of the town at a specific position or null if not
     * existing.
     *
     * @param p
     * @return
     */
    public String getTownAt(MapPosition p) {
        for (Province province : getAllProvinces()) {
            if (province.getTownPosition().equals(p)) {
                return province.toString();
            }
        }
        return null;
    }

    /**
     * Returns the province at a specific position.
     *
     * @param p
     * @return
     */
    public Province getProvinceAt(MapPosition p) {
        int id = getTile(p).provinceID;
        for (Nation nation : nations) {
            for (Province province : nation.getProvinces()) {
                if (id == province.getID()) {
                    return province;
                }
            }
        }
        return null;
    }

    /**
     * Calculates neighbored positions for a given map position of a map in
     * staggered layout (i.e. every second row is shifted by half a tile) and
     * for a given transition, where only 3 transitions are actually taken into
     * account so far.
     *
     * The new position might not be on the map any more.
     *
     * @param p the position
     * @param transition the transition
     * @return the position of the neighbor
     */
    private MapPosition getNeighbourPosition(MapPosition p, TilesTransition transition) {
        int row, column, shift;
        switch (transition) {
        case East:
            row = p.row;
            column = p.column + 1;
            break;
        case West:
            row = p.row;
            column = p.column - 1;
            break;
        case NorthWest:
            row = p.row - 1;
            shift = p.row % 2 == 0 ? 1 : 0;
            column = p.column - shift;
            break;
        case NorthEast:
            row = p.row - 1;
            shift = p.row % 2 == 0 ? 1 : 0;
            column = p.column + 1 - shift;
            break;
        case SouthEast:
            row = p.row + 1;
            shift = p.row % 2 == 0 ? 1 : 0;
            column = p.column + 1 - shift;
            break;
        case SouthWest:
            row = p.row + 1;
            shift = p.row % 2 == 0 ? 1 : 0;
            column = p.column - shift;
            break;
        default:
            row = -1;
            column = -1;
        }
        return new MapPosition(row, column);
    }
    
    public boolean isSameTerrain(MapPosition p, TilesTransition t) {
        MapPosition q = getNeighbourPosition(p, t);
        if (containsPosition(p) && containsPosition(q) && map[p.row][p.column].terrainID == map[q.row][q.column].terrainID) {
            return true;
        }
        return false;
    }
    
    public boolean isSameResource(MapPosition p, TilesTransition t) {
        MapPosition q = getNeighbourPosition(p, t);
        if (containsPosition(p) && containsPosition(q) && map[p.row][p.column].resourceID != Settings.RESOURCE_NONE && map[p.row][p.column].resourceID == map[q.row][q.column].resourceID) {
            return true;
        }
        return false;
    }    

    /**
     * Returns the border for a given position and a given tile transition.
     *
     * @param p the position
     * @param transition the transition
     * @return the type of the border
     */
    public TilesBorder getBorder(MapPosition p, TilesTransition transition) {
        // position has to belong to the map and there must be a province
        if (!containsPosition(p) || getTile(p).provinceID == Province.NONE) {
            return TilesBorder.None;
        }
        MapPosition p2 = getNeighbourPosition(p, transition);
        // neigboured postion must belong and there must be a province
        if (!containsPosition(p2) || getTile(p2).provinceID == Province.NONE) {
            return TilesBorder.None;
        }
        if (getTile(p).provinceID != getTile(p2).provinceID) {
            // at least province change, detect nation change first, since nation border has priority
            // we can compare object identities here
            if (getNationAt(p) != getNationAt(p2)) {
                // nation is different, return nation border
                return TilesBorder.Nation;
            }
            return TilesBorder.Province;
        }
        // both tiles belong to same province
        return TilesBorder.None;
    }

    public boolean hasRailRoad(MapPosition p, TilesTransition transition) {
        if (!containsPosition(p)) {
            return false;
        }
        return (getTile(p).railroadConfig & (1 << transition.order())) != 0;
    }

    /**
     * Returns the title of the scenario.
     *
     * @return the title
     */
    public String getTitle() {
        return properties.get("title");
    }

    /**
     * Sets the title of the scenario.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        properties.put("title", title);
    }

    /**
     * Adds a listener.
     *
     * @param l the listener
     */
    public void addScenarioChangedListener(ScenarioChangedListener l) {
        listeners.add(l);
    }

    /**
     * Removes a listener.
     *
     * @param l the listener
     */
    public void removeScenarioChangedListener(ScenarioChangedListener l) {
        listeners.remove(l);
    }

    /**
     * Tells all listeners that a specific tile has changed.
     *
     * @param p the position of the tile
     */
    private void fireTileChanged(MapPosition p) {
        for (ScenarioChangedListener l : listeners) {
            l.tileChanged(p);
        }
    }

    /**
     * Tells all listeners that everything has changed.
     */
    private void fireScenarioChanged() {
        for (ScenarioChangedListener l : listeners) {
            l.scenarioChanged();
        }
    }

    /**
     * Export to XML.
     *
     * @return
     */
    @Override
    public Node toXML() {
        Node parent = new Node(XML_NAME);

        // update sizes and write properties
        properties.putInt("rows", rows);
        properties.putInt("columns", columns);
        parent.appendChild(properties.toXML());

        Node child = new Node("Maps");
        parent.appendChild(child);

        // terrain map
        BitBuffer terrainMapBuffer = new BitBuffer(BITSIZE_TERRAINID * rows * columns);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                terrainMapBuffer.add(map[row][column].terrainID, BITSIZE_TERRAINID);
            }
        }
        Node schild = new Node("Terrains");
        schild.appendChild(terrainMapBuffer.toXMLString());
        child.appendChild(schild);

        // resources map
        BitBuffer resourcesMapBuffer = new BitBuffer((BITSIZE_RESOURCEID + 1) * rows * columns);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                resourcesMapBuffer.add(map[row][column].resourceID, BITSIZE_RESOURCEID);
                resourcesMapBuffer.add(map[row][column].resourceVisible);
            }
        }
        schild = new Node("Resources");
        schild.appendChild(resourcesMapBuffer.toXMLString());
        child.appendChild(schild);

        // provinces
        BitBuffer provinceMapBuffer = new BitBuffer(BITSIZE_PROVINCEID * rows * columns);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                provinceMapBuffer.add(map[row][column].provinceID, BITSIZE_PROVINCEID);
            }
        }
        schild = new Node("Provinces");
        schild.appendChild(provinceMapBuffer.toXMLString());
        child.appendChild(schild);

        // railroads
        BitBuffer railMapBuffer = new BitBuffer(BITSIZE_RAILROAD_CONFIG * rows * columns);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                railMapBuffer.add(map[row][column].railroadConfig, BITSIZE_RAILROAD_CONFIG);
            }
        }
        schild = new Node("Railroads");
        schild.appendChild(railMapBuffer.toXMLString());
        child.appendChild(schild);

        // river overlay
        BitBuffer riverBuffer = new BitBuffer(BITSIZE_RIVERID * rows * columns);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                riverBuffer.add(map[row][column].riverID, BITSIZE_RIVERID);
            }
        }
        schild = new Node("Rivers");
        schild.appendChild(riverBuffer.toXMLString());
        child.appendChild(schild);

        // nation list
        parent.appendChild(nations.toXML());

        return parent;
    }

    /**
     * Import from XML.
     *
     * @param parent
     */
    @Override
    public void fromXML(Node parent) {

        // TODO clear (?) neccessary?
        // TODO we could also set as new of calling clear
        clear();

        parent.checkNode(XML_NAME);

        // get properties and readout sizes
        properties.fromXML(parent.getFirstChild(XProperty.XML_NAME));
        rows = properties.getInt("rows");
        columns = properties.getInt("columns");
        int size = rows * columns;
        map = new Tile[rows][columns];

        Node child = parent.getFirstChild("Maps");

        // readingt of terrain map
        String content = child.getFirstChild("Terrains").getValue();
        BitBuffer terbuffer = BitBuffer.fromXMLString(content);
        terbuffer.trimTo(BITSIZE_TERRAINID * size);

        // readingt of terrain map
        content = child.getFirstChild("Resources").getValue();
        BitBuffer resbuffer = BitBuffer.fromXMLString(content);
        resbuffer.trimTo((BITSIZE_RESOURCEID + 1) * size);

        // reading of provinces map
        // TODO if no such element returns null and getValue will throw exception
        content = child.getFirstChild("Provinces").getValue();
        BitBuffer probuffer = BitBuffer.fromXMLString(content);
        probuffer.trimTo(BITSIZE_PROVINCEID * size);

        // reading of railroads map
        content = child.getFirstChild("Railroads").getValue();
        BitBuffer railbuffer = BitBuffer.fromXMLString(content);
        railbuffer.trimTo(BITSIZE_RAILROAD_CONFIG * size);

        // reading of rivers map
        content = child.getFirstChild("Rivers").getValue();
        BitBuffer riverbuffer = BitBuffer.fromXMLString(content);
        riverbuffer.trimTo(BITSIZE_RIVERID * size);

        // TODO test size of string with size
        // TODO more checks (positivity)
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                Tile tile = new Tile();
                tile.terrainID = terbuffer.get(BITSIZE_TERRAINID);
                tile.resourceID = resbuffer.get(BITSIZE_RESOURCEID);
                tile.resourceVisible = resbuffer.get();
                tile.provinceID = probuffer.get(BITSIZE_PROVINCEID);
                tile.railroadConfig = railbuffer.get(BITSIZE_RAILROAD_CONFIG);
                tile.riverID = riverbuffer.get(BITSIZE_RIVERID);
                map[row][column] = tile;
            }
        }

        // just a test
        if (terbuffer.size() != 0) {
            // TODO more in buffer... not good, terrainMapBuffer also
        }

        // reading of nations
        nations.fromXML(parent.getFirstChild(XML_NAME_NATIONS));

        // Of course everything has changed.
        fireScenarioChanged();
    }

    /**
     * Just for ensuring they have a unique id.
     *
     * @param name
     * @return
     */
    public Province createProvince(String name) {
        // get new id first
        SortedSet<Integer> ids = new TreeSet<>();
        for (Nation nation : nations) {
            for (Province province : nation.getProvinces()) {
                ids.add(province.getID());
            }
        }
        int id = 1;
        if (!ids.isEmpty() && ids.first() == 1) {
            id++;
            while (ids.contains(id)) {
                id++;
            }
        }
        // make new province and return
        return new Province(id, name);
    }
}
