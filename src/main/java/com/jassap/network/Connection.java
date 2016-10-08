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

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.jassap.exceptions.ConnectionException;
import com.jassap.exceptions.ConnectionLostException;

/**
 * Clase para gestionar las conexiones a partir de un socket
 * @author danjian
 */
public abstract class Connection implements Runnable {
	private Socket socket;
	
	public Connection(Socket socket) {
		this.socket = socket;
	}
	
	/**
	 * @return Direccion IP en formato x.x.x.x
	 */
	public String getAddress() {
		return socket.getInetAddress().getHostAddress();
	}
		
	/**
	 * @return Estado de la conexion. true si esta activa y funciona de forma
	 * correcta.
	 */
	public boolean isAlive() {
		if(socket.isClosed()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Cierra la conexion(socket)
	 * @return true si la conexion se cerro
	 */
	public boolean close() {
		try {
			socket.close();
			//cc.interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return socket.isClosed();
	}
	
	/**
	 * Envia un objeto a traves del socket
	 * @param p El objeto a enviar debe ser una instancia de DataPacket
	 * @return True si envio el paquete
	 * @throws ConnectionLostException si la conexion se ha perdido o el socket
	 * fue cerrado
	 */
	public boolean sendPacket(Packet p) {
		if(socket.isClosed() || !isAlive() || p == null){
			return false;
		}
		
		try {
			OutputStream os = socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.flush();
			oos.writeObject(p);
			oos.flush(); // !
			return true;
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Recibe datos del socket y lo convierte en un objeto de tipo DataPacket 
	 * @return Devuelve el objeto DataPacket o null si hubo un error
	 */
	public Packet readPacket() throws ConnectionException {
		if(socket.isClosed() || !isAlive()){
			throw new ConnectionLostException("No se pueden recibir paquetes "
					+ "porque la conexion esta cerrada.");
		}
		
		Packet packet = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			packet = (Packet) ois.readObject();
		} catch (SocketException e) {
			//e.printStackTrace();
			//close();
			throw new ConnectionException("Se produjo un problema al intentar"
					+ " recibir un paquete, es posible que la conexion este cerrada");
		} catch (EOFException e) {
			e.printStackTrace();
			//close();
			throw new ConnectionException("Connection lost.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return packet;
	}
	
	public abstract void processPacket(DataPacket dataPacket);
	
	@Override
	public void run() {
		while(isAlive()) {
			Packet p = null;
			try {
				p = readPacket();
			} catch (ConnectionLostException e) {
				e.printStackTrace();
				break;
			} catch (ConnectionException e) {
				e.printStackTrace();
				break;
			}
			
			if(p != null) {
				processPacket(new DataPacket(this, p));
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
	
	
	/*
	 * new Thread(new Runnable() {
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
	 * 
	 * */
	 
}