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
package com.jassap.chat;

import java.util.ArrayList;
import java.util.List;

import org.omg.CosNaming.IstringHelper;

import com.jassap.chat.states.Ban;
import com.jassap.chat.states.Mute;
import com.jassap.chat.states.State;
import com.jassap.network.User;
import com.jassap.network.packets.RoomMessage;
import com.jassap.network.packets.ServerMessage;

public class Room {
	private String name;
	private String description;
	private int maxUsers;
	private List<String> mods = new ArrayList<String>();
	private List<User> users = new ArrayList<User>();
	private List<State> mutedBans = new ArrayList<State>();
	private boolean roomMuted = false;

	public Room(String name, String description, int maxUsers) {
		this.name = name;
		this.description = description;
		this.maxUsers = maxUsers;
	}

	public Room(String name) {
		this(name, "", 200);
	}

	/**
	 * Mensaje para toda la sala
	 * 
	 * @param from
	 * @param message
	 */
	public void difusion(String from, String message) {
		User u = null;
		RoomMessage rm = null;

		// Si el mensaje no viene de system, hacemos comprobaciones
		if (!from.equals("System")) {
			u = getUser(from);

			// Usuario no encontrado
			if (u == null) {
				return;
			}

			Mute mute = isMuted(u.getAccount().getUser());
			// Esta muteado
			if (mute instanceof Mute) {
				ServerMessage sm = new ServerMessage(
						"You are muted from this room. time left:"
								+ mute.timeLeft());
				u.getConnection().sendPacket(sm);
				return;
			}

			// Sala muteada
			if (isRoomMuted()) {
				ServerMessage sm = new ServerMessage("Room is muted.");
				u.getConnection().sendPacket(sm);
				return;
			}

			rm = new RoomMessage(name, from, message, u.getAccount().getRole());
		} else {
			rm = new RoomMessage(name, from, message, Roles.SYSTEM);
		}

		for (User user : users) {
			user.getConnection().sendPacket(rm);
		}
	}

	// Pone o quita mute a la sala
	public void muteRoom(boolean mute) {
		roomMuted = mute;
	}

	public boolean isRoomMuted() {
		return roomMuted;
	}

	/**
	 * Devuelve una lista con los nombres de los usuarios de la sala
	 * 
	 * @return
	 */
	public List<String> getRoomUsernames() {
		List<String> list = new ArrayList<String>();

		for (User user : users) {
			list.add(user.getAccount().getUser());
		}

		return list;
	}

	/**
	 * @return La lista con los nombres de los moderadores
	 */
	public List<String> getMods() {
		return new ArrayList<String>(mods);
	}

	/**
	 * Agrega un usuario a la sala
	 * 
	 * @param user
	 */
	public void addUser(User user) {
		// Si el usuario ya esta en la sala ...
		if(users.contains(user)) {
			return;
		}
		
		Ban ban = isBanned(user.getAccount().getUser());

		if (ban instanceof Ban) {
			ServerMessage sm = new ServerMessage(
					"You are banned from this room." + " time left:"
							+ ban.timeLeft());
			user.getConnection().sendPacket(sm);
			return;
		}

		difusion("System", user.getAccount().getUser() + " entra en la sala.");
		users.add(user);
	}

	public void removeUser(User user) {
		users.remove(user);
		difusion("System", user.getAccount().getUser() + " sale de la sala.");
	}

	/**
	 * Obtiene un usuario de la sala
	 * 
	 * @param username
	 *            el nombre del usuario que buscamos
	 * @return Un objeto de tipo User
	 */
	public User getUser(String username) {
		for (User user : users) {
			if (user.getAccount().getUser().equals(username)) {
				return user;
			}
		}
		return null;
	}

	/**
	 * Comprueba si un usuario esta baneado
	 * 
	 * @param user
	 * @return
	 */
	public Ban isBanned(String user) {
		for (State state : mutedBans) {
			if (state instanceof Ban) {
				Ban ban = (Ban) state;
				if (user.equals(ban.getUser())) {
					return ban;
				}
			}
		}
		return null;
	}

	/**
	 * Comprueba si un usuario esta muteado
	 * 
	 * @param user
	 *            El usuario a comprobar
	 * @return null si no esta muteado y objeto Mute si lo esta
	 */
	public Mute isMuted(String user) {
		for (State state : mutedBans) {
			if (state instanceof Mute) {
				Mute mute = (Mute) state;
				if (user.equals(mute.getUser())) {
					return mute;
				}
			}
		}
		return null;
	}

	public void addRoomModerator(String rm) {
		mods.add(rm);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	public int getMaxUsers() {
		return maxUsers;
	}

	public void setMaxUsers(int max) {
		this.maxUsers = max;
	}
}
