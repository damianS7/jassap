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
public abstract class PacketHandler implements Runnable {
	protected List<Packet> packetQueue = new ArrayList<Packet>();
	protected boolean keepRunning = true;
	
	// Devuelve la lista "clonada" de los paquetes que hay en la cola
	private List<Packet> getQueuePackets() {
		return new ArrayList<Packet>(packetQueue);
	}
	
	// Añade un paquete a la cola
	protected void queuePacket(Packet packet) {
		packetQueue.add(packet);
	}
	
	/*
	 * Determina una accion para cada paquete
	 */
	protected void handlePacket(Packet p) {
		
		// Los paquetes ping se responden con paquetes "pong"
		if (p instanceof Ping) {
			p.getSender().sendPacket(new Pong(p.getSender()));
			return;
		}
		
		// Los paquetes pong no se responden
		if (p instanceof Pong) {
			return;
		}
	}
	
	// Detiene el hilo que procesa los paquetes
	protected void stopRunning() {
		keepRunning = false;
	}
	
	@Override
	public void run() {
		
		while(keepRunning) {
			// Si hay paquetes en cola ...
			if(!packetQueue.isEmpty()) {
				
				/*
				 * Iteramos sobre getQueuePackets() para no tener problemas con
				 * los hilos y el acceso a la lista, pues si trabamos directamente
				 * sobre ella al borrar puede haber problemas de concurrencia
				 */
				for (Packet packet : getQueuePackets()) {
					handlePacket(packet);
					packetQueue.remove(packet);
				}
			}
		}
	}
}
