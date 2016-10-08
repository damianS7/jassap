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

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

import com.jassap.database.Account;
import com.jassap.network.packets.Disconnect;

public class Client {
	protected ClientUser user;
	
	public ClientUser getClientUser() {
		return user;
	}
	
	/**
	 * Conecta con el servidor
	 * @return true si conecto con exito
	 */
	public boolean connect() {
		try {
			Account acc = new Account(JassapClient.clientProperties.getUsername(),
					JassapClient.clientProperties.getPassword());
			
			ClientConnection cc = new ClientConnection(new Socket(
					JassapClient.clientProperties.getAddress(),
					JassapClient.clientProperties.getPort()));
			new Thread(cc).start();
			user = new ClientUser(cc, acc);
			// connection.getSocket().setSoTimeout(1000); // MS
			JassapClient.ui.setStatus("Connected");
			return true;
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @return true si la conexion aun esta establecida y funciona
	 */
	public boolean isConnected() {
		// Si connection es null es que aun no se instancio
		if(user != null) {
			return user.getConnection().isAlive();
		}
		return false;
	}

	/**
	 * @return True si se cerro la conexion
	 */
	public boolean disconnect() {
		// Avisa al servidor de que se cierra la conexion
		user.getConnection().sendPacket(new Disconnect());
		
		if (user.getConnection().close()) {
			JassapClient.ui.setStatus("Disconnected");
			return true;
		}
		
		return false;
	}

}
