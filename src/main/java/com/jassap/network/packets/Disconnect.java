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
 * Paquete enviado por el cliente para avisar al servidor de que se desconecta.
 * Si el paquete es enviado por el servidor entonces, ordena al cliente cerrar
 * la conexion
 * @author danjian
 */
public class Disconnect extends Packet {
	private static final long serialVersionUID = -552390140544585668L;
	private String reason;
	
	public Disconnect() {
		this("");
	}
	
	public Disconnect(String reason) {
		this.reason = reason;
	}
	
	public String getReason() {
		return reason;
	}
	
}
