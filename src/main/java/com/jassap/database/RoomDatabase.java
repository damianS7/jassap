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
package com.jassap.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jassap.chat.Roles;
import com.jassap.chat.Room;
import com.jassap.chat.RoomModerator;

/**
 * Clase para interactuar con el fichero .json que contiene las salas junto con
 * la informacion de estas
 * 
 * @author danjian
 */
public class RoomDatabase extends JsonDatabase {
	private static final Logger LOGGER = Logger.getLogger(RoomDatabase.class
			.getName());
	private List<Room> rooms = new ArrayList<Room>();

	public RoomDatabase(File file) {
		super(file);
	}

	/**
	 * @return Una copia de las salas cargadas desde el fichero .json
	 */
	public List<Room> getRooms() {
		return new ArrayList<Room>(rooms);
	}

	/**
	 * @param roomName Nombre de la sala a buscar
	 * @return La sala o null si no se encuentra
	 */
	public Room getRoom(String roomName) {
		for (Room room : rooms) {
			System.out.println(roomName);
			if (room.getName().equals(roomName)) {
				return room;
			}
		}
		return null;
	}

	/**
	 * Añade una nueva sala a la base de datos. Debes usar el metodo
	 * save() para guardar los cambios
	 * @param room La sala que queremos añadir
	 */
	public void addRoom(Room room) {
		rooms.add(room);
	}

	/**
	 * Borra una sala 
	 * @param roomName el nombre de la sala a borrar
	 */
	public void removeRoom(String roomName) {
		Room r = getRoom(roomName);
		if (r != null) {
			removeRoom(r);
		}
	}
	
	/**
	 * Borra una sala 
	 * @param room La sala a borrar
	 */
	public void removeRoom(Room room) {
		rooms.remove(room);
	}

	
	public int load() {
		super.load();

		for (String roomName : jsonData.keySet()) {
			Room r = new Room(roomName);

			try {
				JSONObject roomJson = jsonData.getJSONObject(roomName);
				r.setDescription(roomJson.getString("description"));
				r.setMaxUsers(roomJson.getInt("limit"));

				JSONArray modsJson = roomJson.getJSONArray("mods");
				for (int i = 0; i < modsJson.length(); i++) {
					String mod = (String) modsJson.get(i);
					RoomModerator rm = new RoomModerator(mod);
					r.addRoomModerator(rm);
				}

				rooms.add(r);
			} catch (JSONException e) {
				LOGGER.severe(e.getMessage());
				continue;
			}
		}

		return rooms.size();
	}

	@Override
	public void save() {
		jsonData = new JSONObject();
		for (Room room : rooms) {
			JSONObject roomData = new JSONObject();
			roomData.put("description", room.getDescription());
			roomData.put("limit", room.getMaxUsers());

			JSONArray mods = new JSONArray();
			for (RoomModerator mod : room.getMods()) {
				mods.put(mod.getName());
			}
			roomData.put("mods", mods);
			jsonData.put(room.getName(), roomData);
		}

		super.save();
	}
}