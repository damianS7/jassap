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

import com.jassap.exceptions.ConnectionException;
import com.jassap.exceptions.ConnectionLostException;

/**
 * Esta clase se usa para identificar a las conexiones establecidas por los
 * usuarios
 * @author danjian
 */
public abstract class User implements Runnable {
	protected Connection conn;
	//protected Privileges priv
	//protected ChatUser
	//protected ServerUser
	
	public User(Connection conn) {
		this.conn = conn;
	}
	
	public abstract void processPacket(Packet p);
	
	public Connection getConnection() {
		return conn;
	}
	
	@Override
	public void run() {
		while(conn.isAlive()) {
			Packet p = null;
			try {
				p = conn.readPacket();
			} catch (ConnectionLostException e) {
				e.printStackTrace();
				break;
			} catch (ConnectionException e) {
				e.printStackTrace();
				break;
			}
			
			if(p != null) {
				processPacket(p);
			}
		}
		
		/*
		Thread t = new Thread(ph);
		t.start();

		// List packet para ir leyendo uno a uno segun order de llegada
		while (cc.isAlive()) {
			// Esperando paquetes
			DataPacket dp = readPacket();
			if (dp instanceof DataPacket) {
				cc.alive();
				Packet packet = new Packet(this, dp);
				ph.queuePacket(packet);
			}
		}
		*/
		
		System.out.println("Conexion terminada");
	}
	
}
