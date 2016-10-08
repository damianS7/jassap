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

import java.net.Socket;

import com.jassap.network.Connection;
import com.jassap.network.DataPacket;
import com.jassap.network.packets.LoginRequest;
import com.jassap.network.packets.RoomsRequest;

public class ClientConnection extends Connection {
	private ClientPacketHandler cph;
	
	public ClientConnection(Socket socket) {
		super(socket);
		cph = new ClientPacketHandler();
	}

	@Override
	public void processPacket(DataPacket dataPacket) {
		cph.handlePacket(dataPacket);
	}

	@Override
	public void run() {
		sendPacket(new LoginRequest(JassapClient.clientProperties.getUsername(),
				JassapClient.clientProperties.getPassword()));
		
		sendPacket(new RoomsRequest());
		super.run();
	}
}
