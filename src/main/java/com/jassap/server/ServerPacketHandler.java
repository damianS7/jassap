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
package com.jassap.server;

import com.jassap.network.Packet;
import com.jassap.network.PacketHandler;

/**
 * Procesa los paquetes que el servidor recibe de los clientes y lo hace en 
 * orden de llegada.
 * @author danjian
 */
public class ServerPacketHandler extends PacketHandler implements Runnable {
	protected boolean keepRunning = true;

	// Detiene el hilo que procesa los paquetes
	protected void stopRunning() {
		keepRunning = false;
	}

	@Override
	protected void handlePacket(Packet p) {

		super.handlePacket(p);
	}

	@Override
	public void run() {
		while (keepRunning) {
			// Si hay paquetes en cola ...
			if (!packetQueue.isEmpty()) {

				/*
				 * Iteramos sobre getQueuePackets() para no tener problemas con
				 * los hilos y el acceso a la lista, pues si trabamos
				 * directamente sobre ella al borrar puede haber problemas de
				 * concurrencia
				 */
				for (Packet packet : getQueuePackets()) {
					handlePacket(packet);
					packetQueue.remove(packet);
				}
			}
		}
	}
}
