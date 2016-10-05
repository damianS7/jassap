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
package com.jassap.server;

import java.util.ArrayList;
import java.util.List;

import com.jassap.chat.Room;
import com.jassap.chat.User;
import com.jassap.network.Connection;

public class ChatServer extends Server {
	private List<User> users = new ArrayList<User>();
	private List<Room> rooms = new ArrayList<Room>();
	
	/*
	 * Devuelve una copia de las salas disponibles en el chat
	 */
	public List<Room> getRooms() {
		return new ArrayList<Room>(rooms);
	}
	
	/*
	 * Devuelve una copia de los usuarios que hay conectados en el servidor
	 */
	public List<User> getUsers() {
		return new ArrayList<User>(users);
	}
	
	/*
	 * Devuelve el numero de usuarios conectados en el servidor
	 */
	public int countUsers() {
		return users.size();
	}
	
	/*
	 * Expulsa del servidor a un usuario cerrando la conexion (socket)
	 */
	public void kick(User user) {
		
	}
	
	/*
	 * Expulsa a todos los usuarios del servidor
	 */
	@Override
	public void kickAll() {
		for (User user : getUsers()) {
			kick(user);
		}
	}

	@Override
	public boolean handleConnection(Connection connection) {
		if(!super.handleConnection(connection)) {
			// Enviar paquete al cliente avisando de que el servidor esta lleno?
		}
		//users.add(new User(connection));
		return false;
	}
	
}
