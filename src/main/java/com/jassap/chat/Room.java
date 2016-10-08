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

import com.jassap.chat.states.Ban;
import com.jassap.chat.states.Mute;
import com.jassap.chat.states.State;
import com.jassap.network.User;
import com.jassap.network.packets.RoomMessage;


public class Room {
	private String name;
	private String description;
	private int maxUsers;
	private List<String> mods = new ArrayList<String>();
	private List<User> users = new ArrayList<User>();
	private List<State> mutedBans = new ArrayList<State>();
	
	public Room(String name, String description, int maxUsers) {
		this.name = name;
		this.description = description;
		this.maxUsers = maxUsers;
	}
	
	public void difusion(String from, String message) {
		RoomMessage rm = new RoomMessage(from, message, name);
		for (User user : users) {
			user.getConnection().sendPacket(rm);
		}
	}
	
	public List<String> getUserList() {
		List<String> list = new ArrayList<String>();
		
		for (User user : users) {
			list.add(user.getAccount().getUser());
		}
		
		return list;
	}
	
	public List<String> getMods() {
		return mods;
	}
	
	public boolean isBanned(String user) {
		for (State u : mutedBans) {
			if(u instanceof Ban) {
				if(user.equals(((Ban) u).getUser())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isMuted(String user) {
		for (State u : mutedBans) {
			if(u instanceof Mute) {
				if(user.equals(((Mute) u).getUser())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Room(String name) {
		this(name, "", 200);
	}
	
	public void addUser(User user) {
		// Check user global privileges para meterlo a roommod?
		users.add(user);
	}
	
	public void removeUser(User user) {
		users.remove(user);
	}
	
	public User getUser(String username) {
		for (User chatUser : users) {
			/*if(chatUser.getName().equals(username)) {
				return chatUser;
			}*/
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
