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

import com.jassap.network.Packet;

/**
 * Este paquete lo envia el servidor y contiene los nuevos mensajes de una sala
 * @author damjian
 */
public class RoomMessage extends Packet {
	private static final long serialVersionUID = 3116327084898693224L;
	private String sender;
	private String message;
	private String room;
	
	public RoomMessage(String sender, String message, String room) {
		this.sender = sender;
		this.message = message;
		this.room = room;
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
