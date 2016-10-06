/*
 * Copyright (C) 2013 by danjian <josepwnz@gmail.com>
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
package com.jassap.client.ui;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.jassap.client.JassapClient;

/**
 * Lista para los usuarios de las salas que implementa eventos para abrir
 * conversaciones al hacer click en el nombre del usuario y un menu popup
 * al hacer click derecho sobre el nombre de un usuario de la lista.
 * 
 * Este menu es para admin/mods y esta restringido a usuarios comunes.
 * @author danjian
 */
public class UserList extends JassapList {
	private static final long serialVersionUID = 3861971951317131589L;
	private JPopupMenu menu;
	private List<String> users = new ArrayList<String>();
	
	public UserList() {
		super();
		menu = new JPopupMenu();
		menu.addMouseListener(this);
		JMenuItem item1 = new JMenuItem("Item");
		menu.add(item1);
	}
	
	public void addUser(String username) {
		users.add(username);
		model.addElement(username);
	}
	
	public void addUsers(List<String> usersc) {
		for (String user : usersc) {
			addUser(user);
		}
	}
	
	public void removeUser(String username) {
		users.remove(username);
		model.removeElement(username);
	}
	
	private void showPopupMenu(MouseEvent e) {
		if (e.isPopupTrigger()) {
			// Numero de fila donde esta el puntero
			//int row = model.rowAtPoint(e.getPoint());
			setSelectedIndex(locationToIndex(e.getPoint()));
			System.out.println("Actions over: " + model.getElementAt(getSelectedIndex()));
			menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		showPopupMenu(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		showPopupMenu(e);
		
		if(e.getClickCount() == 2) {
			
			JassapClient.ui.addTab(new ConversationTab(model.get(getSelectedIndex())));
			//joinRoom(e);
			//JassapClient._client.joinRoom(list.getSelectedValue());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
