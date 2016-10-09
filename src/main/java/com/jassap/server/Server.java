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

/**
 * Base del servidor
 * 
 * @author danjian
 */
public abstract class Server implements Runnable {
	private final Logger LOGGER = Logger.getLogger(Server.class.getName());
	
	/**
	 * Flag de "apagado en curso", a partir del momento que este true el
	 * servidor empezara el proceso de apagado.
	 */
	private boolean shuttingDown = false;

	// Flag de estado del servidor
	private boolean running = false;

	// Socket del servidor para aceptar conexiones nuevas
	protected ServerSocket serverSocket;

	// Limite de conexiones permitidas en el servidor
	protected int maxConnections = 200;

	// Array con usuarios con conexiones abiertas en el servidor
	protected List<ServerConnection> connections = new ArrayList<ServerConnection>();

	/*
	 * Procesa los paquetes entrantes al servidor y determina una accion para
	 * cada uno de ellos
	 */
	protected ServerPacketHandler sph;

	public Server() {
		sph = new ServerPacketHandler();
	}
	
	/*
	 * El numero maximo de conexiones entrantes que aceptara el servidor
	 */
	public int getMaxConnections() {
		return maxConnections;
	}

	/*
	 * Cuenta el numero de conexiones abiertas actualmente
	 */
	public int getCountConnections() {
		return connections.size();
	}
	
	/*
	 * Devuelve una copia de las que hay en el servidor
	 */
	public List<ServerConnection> getConnections() {
		return new ArrayList<ServerConnection>(connections);
	}

	/*
	 * True si el servidor esta apagandose
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

	/**
	 * Cierra todas las conexiones abiertas en el servidor
	 */
	public void kickAll() {
		for (ServerConnection connection : getConnections()) {
			connection.close();
		}
	}

	/**
	 * Reinicia el servidor
	 * 
	 * @return Devuelve true solo si el servidor se detuvo e inicio sin
	 *         incidencias
	 */
	public boolean restart() {
		if(!stop()) {
			return false;
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (start()) {
			return true;
		}

		return false;
	}

	/**
	 * Inicia el servidor
	 * 
	 * @return true si el servidor estaba detenido y es iniciado con exito
	 */
	public boolean start() {
		if (isRunning()) {
			return false;
		}

		try {
			InetAddress addr = InetAddress
					.getByName(JassapServer.serverProperties.getAddress());
			serverSocket = new ServerSocket(
					JassapServer.serverProperties.getPort(), 0, addr);
		} catch (IOException e) {
			LOGGER.severe("Error al iniciar el servidor.");
			e.printStackTrace();
			return false; // No inicio
		}

		JassapServer.ui.outputLog.append("Servidor escuchando en "
				+ JassapServer.serverProperties.getAddress() + ":"
				+ JassapServer.serverProperties.getPort());

		/*
		 * En caso de que el servidor se inicie varias veces, pueden quedar
		 * paquetes antiguos en la cola, por lo que debe ser vaciada cada vez
		 * que se inicia el servidor
		 */
		sph.clearQueue();

		new Thread(this).start();
		running = true;
		new Thread(sph).start();
		return true;
	}

	/**
	 * Detiene el servidor
	 * 
	 * @return true si el servidor estaba iniciado y se detuvo correctamente
	 */
	public boolean stop() {
		if (!isRunning()) {
			// El servidor no esta corriendo, nada que hacer
			return false;
		}

		// Señal de apagado en curso ...
		shuttingDown = true;

		try {
			serverSocket.close();
		} catch (IOException e) {
			LOGGER.severe("El servidor no pudo ser detenido");
			e.printStackTrace();
			return false;
		}

		// Deja de procesar los paquetes recibidos
		sph.stopRunning();
		
		// Se cierran las conexion de todos los que hubiera conectados
		kickAll();

		// Servidor detenido
		running = false;
		shuttingDown = false;
		return true;
	}

	/**
	 * Define que acciones se toman una vez se ha recibido una nueva conexion
	 * @param connection
	 * @return true si la conexion se manejo con exito
	 */
	public boolean addConnection(ServerConnection connection) {
		// Si hay demasiadas conexiones se rechaza
		if (getCountConnections() >= maxConnections) {
			connection.close();
			return false;
		}
		connections.add(connection);
		return true;
	}

	/*
	 * Hilo que se mantiene mientras el servidor este encendido
	 */
	public void run() {
		while (running) {
			/*
			 * Si el servidor se esta apagando, el loop se mantiene vivo hasta
			 * que acabe de cerrar todas las conexiones abiertas
			 */
			if (isShuttingDown()) {
				continue;
			}

			try {
				Socket s = serverSocket.accept();
				ServerConnection sc = new ServerConnection(s);
				new Thread(sc).start();
				addConnection(sc);
			} catch (SocketException e) {
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Servidor detenido");
	}
}
