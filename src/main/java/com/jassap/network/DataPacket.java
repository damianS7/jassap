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
package com.jassap.network;

public class DataPacket {
	/*
	 * Esta es la conexion de la que procede el paquete y la que se ha de usar
	 * para responder. A traves de esta conexion se envia la respuesta.
	 * 
	 * Ej: Un paquete procedente de la conexion X ha de responder usando
	 * la misma conexion por la que el paquete llego.
	 */
	private Connection sender;
	private Packet packet;
	
	public DataPacket(Connection sender, Packet packet) {
		this.packet = packet;
		this.sender = sender;
	}
	
	public Packet getPacket() {
		return packet;
	}
	
	public Connection getSender() {
		return sender;
	}
}
