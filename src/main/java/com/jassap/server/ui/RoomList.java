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
package com.jassap.server.ui;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import com.jassap.chat.Room;
import com.jassap.client.ui.JassapList;

public class RoomList extends JassapList {
private List<String> rooms = new ArrayList<String>();
	
	public RoomList() {
		super();
	}
	
	public RoomList(List<Room> Rooms) {
		super();
		
		for (Room Room : Rooms) {
			addRoom(Room.getName());
		}
	}
	
	public void addRoom(String acc) {
		rooms.add(acc);
		model.addElement(acc);
	}
	
	public void removeRoom(String acc) {
		rooms.remove(acc);
		model.remove(getSelectedIndex());
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
