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
 * Paquete enviado por el cliente y reenviado por el servidor al destinatario
 * del mensaje
 * @author danjian
 */
public class ConversationMessage extends Packet {
	private static final long serialVersionUID = -1868074765721429692L;
	private String message;
	private String from;
	private String to;
	
	public ConversationMessage(String from, String to, String message) {
		this.from = from;
		this.to = to;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getFrom() {
		return from;
	}
	
	public String getTo() {
		return to;
	}
}
