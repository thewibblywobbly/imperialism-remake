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
package org.iremake.common.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

/**
 * Static methods for converting standard variables to DOM and forth
 *
 * appendChild(String) goes directly into the tag
 */
public class XMLHandler {

    /**
     * To avoid instantiation.
     */
    private XMLHandler() {
    }

    /**
     * @param map
     * @param name
     * @return
     */
    // TODO does not work with null entries
    public static Element MapToXML(Map<String, String> map, String name) {

        // new parent element
        Element parent = new Element(name);

        // loop over map entries and add new elements with according attributes
        for (Map.Entry<String, String> e : map.entrySet()) {
            Element entry = new Element("entry");
            entry.addAttribute(new Attribute("key", e.getKey()));
            entry.addAttribute(new Attribute("value", e.getValue()));
            parent.appendChild(entry);
        }
        return parent;
    }

    /**
     *
     * @param element
     * @return
     * @throws Exception
     */
    public static Map<String, String> XMLToMap(Element element) {

        Elements children = element.getChildElements();

        Map<String, String> map = new HashMap<>(children.size());

        for (int i = 0; i < children.size(); i++) {
            Element e = children.get(i);
            if (e.getAttribute("key") == null || e.getAttribute("value") == null) {
                throw new XMLException("Attributes missing in xml element during conversion to map.");
            }
            map.put(e.getAttributeValue("key"), e.getAttributeValue("value"));
        }

        return map;
    }

    /**
     *
     * @param list
     * @param name
     * @return
     */
    public static Element ListToXML(List<String> list, String name) {
        Element parent = new Element(name);
        for (String s : list) {
            Element child = new Element("e");
            child.appendChild(s);
            parent.appendChild(child);
        }
        return parent;
    }

    /**
     *
     * @param element
     * @return
     */
    public static List<String> XMLToList(Element element) {

        // get all child elements
        Elements children = element.getChildElements();
        int n = children.size();

        // put them one by one in the natural order into a new array list
        List<String> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            list.add(children.get(i).getValue());
        }
        return list;
    }
}