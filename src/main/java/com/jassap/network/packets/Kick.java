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
 * Al recibir este paquete el usuario es kickeado de una sala
 * @author danjian
 */
public class Kick extends Packet {
	private static final long serialVersionUID = -8983058122845285595L;
	private String by;
	private String reason;
	private String room;
	private String target;
	
	public Kick(String by, String target, String reason, String room) {
		this.target = target;
		this.reason = reason;
		this.room = room;
		this.by = by;
	}
	
	public String getTarget() {
		return target;
	}
	
	public String getReason() {
		return reason;
	}
	
	public String getBy() {
		return by;
	}
	
	public String getRoom() {
		return room;
	}
}
