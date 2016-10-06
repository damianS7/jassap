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

import com.jassap.network.Connection;
import com.jassap.network.Packet;
import com.jassap.network.User;

/**
 * 
 * @author danjian
 */
public class ServerUser extends User {
	private int loginTimeout = 60;
	private boolean logged = false;
	
	public ServerUser(Connection connection) {
		super(connection);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// El usuario tiene X segundos para logearse o su conexion
				// sera cerrada
				for (int i = 0; i < loginTimeout; i++) {
					if(logged) {
						break;
					}
				}
				
				if(!logged) {
					//close();
				}
				
			}
		}).start();
	}
	
	@Override
	public void processPacket(Packet p) {
		JassapServer.server.sph.handlePacket(p);
	}

}
