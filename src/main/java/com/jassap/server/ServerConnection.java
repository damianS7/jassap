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

import java.net.Socket;

import com.jassap.network.Connection;
import com.jassap.network.DataPacket;

/**
 * Esta clase hereda de connection y lo unico que hace es implementar
 * la forma en que los paquetes son procesados.
 * @author danjian
 */
public class ServerConnection extends Connection {

	public ServerConnection(Socket socket) {
		super(socket);
	}

	@Override
	public void processPacket(DataPacket dp) {
		JassapServer.server.sph.handlePacket(dp);
	}

}
