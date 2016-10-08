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

import java.util.ArrayList;
import java.util.List;

import com.jassap.network.packets.Ping;
import com.jassap.network.packets.Pong;

/**
 * Clase para procesar los paquetes que se envian/reciben entre servidor/cliente
 * @author danjian
 */
public abstract class PacketHandler {
	protected List<DataPacket> packetQueue = new ArrayList<DataPacket>();
	
	// Devuelve la lista "clonada" de los paquetes que hay en la cola
	protected List<DataPacket> getQueuePackets() {
		return new ArrayList<DataPacket>(packetQueue);
	}
	
	// Añade un paquete a la cola
	public void queuePacket(DataPacket packet) {
		packetQueue.add(packet);
	}
	
	// Vacia la cola borrando todos los paquetes
	public void clearQueue() {
		packetQueue.clear();
	}
	
	/*
	 * Determina una accion para cada paquete
	 */
	protected void handlePacket(DataPacket dp) {
		Packet p = dp.getPacket();
		
		// Los paquetes ping se responden con paquetes "pong"
		if (p instanceof Ping) {
			System.out.println("Ping received!");
			dp.getSender().sendPacket(new Pong());
		}
		
		// Los paquetes pong no se responden
		if (p instanceof Pong) {
			System.out.println("Pong received!");
			return;
		}
		
	}
}
