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

import com.jassap.client.JassapClient;

/**
 * Una lista con los nombres de las salas disponibles. Implementa metodos
 * para añadir y borrar salas facilmente. Ademas de un evento para entrar
 * en las salas al hacer doble click en el elemento de la lista
 * @author danjian
 */
public class RoomList extends JassapList {
	private static final long serialVersionUID = 961840196718831753L;
	private List<String> rooms = new ArrayList<String>();

	public RoomList() {
		super();
	}
	
	public void addRooms(List<String> rooms) {
		clearRooms();
		for (String room : rooms) {
			addRoom(room);
		}
	}

	public void addRoom(String room) {
		rooms.add(room);
		model.addElement(room);
	}

	public void clearRooms() {
		rooms.clear();
		model.clear();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// joinRoom(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		if (e.getClickCount() == 2) {
			JassapClient.client.joinRoom(getSelectedValue());
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
