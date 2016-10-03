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

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.jassap.chat.User;
import com.jassap.network.Connection;

public abstract class Server implements Runnable {
	private final Logger LOGGER = Logger.getLogger(Server.class.getName());
	private boolean running = false;
	private ServerSocket serverSocket;
	private ServerPacketHandler serverPacketHandler;
	private List<Connection> connections = new ArrayList<Connection>();
	
	/**
	 * marca el servidor como "apagandose", a partir de este momento ningun
	 * nuevo cliente sera aceptado
	 */
	private boolean shuttingDown = false;

	/*
	 * True si el servidor ha recibido la señal para apagarse
	 */
	public boolean isShuttingDown() {
		return shuttingDown;
	}
	
	/*
	 * True si el servidor esta iniciado
	 */
	public boolean isRunning() {
		return running;
	}
	
	public List<Connection> getConnections() {
		return new ArrayList<Connection>(connections);
	}
	
	/**
	 * Cierra todas las conexiones abiertas en el servidor
	 */
	public void kickAll() {
		for (Connection connection : getConnections()) {
			connection.close();
		}
	}
	
	/**
	 * Reinicia el servidor
	 * @return Devuelve true solo si el servidor se detuvo e inicio
	 * sin incidencias
	 */
	public boolean restart() {
		if(stop() && start()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Inicia el servidor
	 * @return true si el servidor estaba detenido y es iniciado con exito
	 */
	public boolean start() {
		if(isRunning()) {
			return false;
		}
		
		try {
			InetAddress addr = InetAddress.getByName(JassapServer.config.getAddress());
			serverSocket = new ServerSocket(JassapServer.config.getPort(), 0, addr);
		} catch (IOException e) {
			LOGGER.severe("Error al iniciar el servidor.");
			e.printStackTrace();
			return false; // No inicio
		}
		
		JassapServer.ui.outputLog.append("Servidor escuchando en "
				+ JassapServer.config.getAddress() + ":" + JassapServer.config.getPort());

		serverPacketHandler = new ServerPacketHandler();
		
		new Thread(this).start();
		running = true;
		new Thread(serverPacketHandler).start();
		return true;
	}

	/**
	 * Detiene el servidor
	 * @return true si el servidor estaba iniciado y se detuvo correctamente
	 */
	public boolean stop() {
		if(!isRunning()) {
			// El servidor no esta corriendo, nada que hacer
			return false;
		}
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			LOGGER.severe("El servidor no pudo ser detenido");
			e.printStackTrace();
			return false;
		}
		
		// No se aceptan nuevas conexiones al servidor
		shuttingDown = true;

		// Se cierran las conexion de todos los que hubiera conectados
		kickAll();
		
		// Servidor detenido
		running = false;
		shuttingDown = false;
		return true;
	}
	
	/**
	 * 
	 * @param connection
	 */
	public abstract void dispatchConnection(Connection connection);
	
	/*
	 * 
	 */
	public void run() {
		Socket clientSocket = null;
		
		while(running) {
			System.out.println("Servidor corriendo ...");
			
			try {
				// Si el servidor se esta apagando no se aceptan nuevas conexiones
				if(!isShuttingDown()) {
					System.out.println("Servidor apagandose ...");
					clientSocket = serverSocket.accept();
					dispatchConnection(new Connection(clientSocket));
				}
			} catch (SocketException e) {
				//e.printStackTrace();
				stop();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		
		shuttingDown = false;
		System.out.println("Servidor detenido");
	}
}
