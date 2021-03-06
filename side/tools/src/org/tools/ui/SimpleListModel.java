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
package org.tools.ui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 * A simple, generic, (read-only) ListModel implementation for JList components
 * based (naturally) on a List.
 */
// TODO make copies if demanded
public class SimpleListModel<E extends Comparable<? super E>> extends AbstractListModel<E> {

    private static final long serialVersionUID = 1L;
    private List<E> content;

    public SimpleListModel() {
        content = new LinkedList<>();
    }

    /**
     *
     * @param list
     */
    public SimpleListModel(List<E> list) {
        content = list;
    }

    public void set(List<E> list) {
        fireIntervalRemoved(this, 0, content.size());
        content = list;
        fireIntervalAdded(this, 0, content.size());
    }

    public void sort() {
        Collections.sort(content);
        fireContentsChanged(this, 0, content.size());
    }

    public void clear() {
        fireIntervalRemoved(this, 0, content.size());
        content.clear();
    }

    @Override
    public int getSize() {
        return content.size();
    }

    @Override
    public E getElementAt(int index) {
        return content.get(index);
    }
}
