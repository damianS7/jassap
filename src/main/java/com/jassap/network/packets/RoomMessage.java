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
package com.jassap.network.packets;

import java.awt.Color;

import com.jassap.chat.Roles;
import com.jassap.network.Packet;

/**
 * Este paquete lo envia el servidor y contiene los nuevos mensajes de una sala
 * @author damjian
 */
public class RoomMessage extends Packet {
	private static final long serialVersionUID = 3116327084898693224L;
	private String sender;
	private Roles senderRole;
	private String message;
	private String room;
	
	public RoomMessage(String sender, String message, String room) {
		this(sender, Roles.USER, message, room);
	}
	
	public RoomMessage(String sender, Roles senderRole, String message, String room) {
		this.sender = sender;
		this.message = message;
		this.room = room;
		this.senderRole = senderRole;
	}
	
	public Color getColor() {
		Color color = Color.BLACK;
		switch (senderRole) {
		case ADMIN:
			color = Color.GREEN;
			break;
		case SUPERMOD:
			color = Color.BLUE;
			break;
		case MOD:
			color = Color.MAGENTA;
			break;
		default:
			break;
		}
		return color;
	}
	
	public Roles getSenderRole() {
		return senderRole;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getRoom() {
		return room;
	}
	
	public String getSender() {
		return sender;
	}
}
