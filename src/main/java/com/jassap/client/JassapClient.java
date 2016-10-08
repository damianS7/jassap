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
package com.jassap.client;

import java.awt.EventQueue;
import java.io.File;

import com.jassap.client.ui.ClientUI;
import com.jassap.network.packets.RoomJoinRequest;
import com.jassap.network.packets.RoomMessage;
/**
 * Clase principal del chat 
 * @author danjian
 */
public class JassapClient extends Client {
	public static final String dirName = "jassap/";
	public static JassapClient client;
	public static ClientProperties clientProperties;
	public static ClientUI ui;
	
	public void joinRoom(String room) {
		RoomJoinRequest rjr = new RoomJoinRequest(room, client.getClientUser().getAccount().getUser());
		client.getClientUser().getConnection().sendPacket(rjr);
	}
	
	public void exitRoom() {
	}
	
	public void sendRoomMessage(String message, String room) {
		RoomMessage rm = new RoomMessage(client.getClientUser().getAccount().getUser(), message, room);
		client.getClientUser().getConnection().sendPacket(rm);
	}
	
	public void sendConversationMessage() {
	}
	
	public static void main(String[] args) throws Exception {
		File actualDir = new File(".");

		// Comprobacion de escritura
		if (!actualDir.canWrite()) {
			throw new Exception("This application needs permmissions to write"
					+ " on the actual folder.");
		}

		// Comprobaciones de ficheros de configuracion y directorios
		File config = new File(dirName);

		// Si el directorio no existe, se procede a crearlo
		if (!config.exists()) {
			if (!config.mkdir()) {
				throw new Exception("No se pudo crear el directorio para "
						+ "la configuracion del servidor");
			}
		}

		// Fichero de configuracion del servidor
		config = new File(dirName + "jassap.properties");

		// Se crea el fichero de configuracion si no existe
		if (!config.exists()) {
			config.createNewFile();
		}

		// Se lanza la UI
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				ui = new ClientUI();
			}
		});

		clientProperties = new ClientProperties(config);
		client = new JassapClient();
	}
}
