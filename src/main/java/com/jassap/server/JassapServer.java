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

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.jassap.chat.Room;
import com.jassap.database.AccountDatabase;
import com.jassap.database.RoomDatabase;
import com.jassap.network.User;
import com.jassap.network.packets.Disconnect;
import com.jassap.server.ui.ServerUI;

/**
 * Esta es la clase que lanza la aplicacion y contiene el servidor del chat
 * @author danjian
 */
public class JassapServer extends Server {
	private static final Logger LOGGER = Logger.getLogger(JassapServer.class
			.getName());
	public static final String dirName = "jassap-server/";
	public static ServerUI ui;
	public static JassapServer server;
	public static ServerProperties serverProperties;
	public static RoomDatabase roomDatabase;
	public static AccountDatabase accountDatabase;
	private List<ServerUser> users = new ArrayList<ServerUser>();
	private List<Room> rooms = new ArrayList<Room>();
	
	public List<Room> getRooms() {
		return new ArrayList<Room>(rooms);
	}
	
	public List<ServerUser> getUsers() {
		return new ArrayList<ServerUser>(users);
	}
	
	public ServerUser getUser(String name) {
		for (ServerUser user : users) {
			if(user.getAccount().getUser().equals(name)) {
				return user;
			}
		}
		return null;
	}
	
	public ServerUser getUserByIp(String ip) {
		for (ServerUser user : users) {
			if(user.getConnection().getAddress().equals(ip)) {
				return user;
			}
		}
		return null;
	}
	
	public Room getRoom(String roomName) {
		for (Room room : rooms) {
			if(room.getName().equals(roomName)) {
				return room;
			}
		}
		
		return null;
	}
	
	public int countUsers() {
		return users.size();
	}
	
	/*
	 * Expulsa del servidor a un usuario cerrando la conexion (socket)
	 */
	public void kick(ServerUser user) {
		System.out.println("kiking");
		removeUser(user);
		Disconnect d = new Disconnect("Server kick.");
		user.getConnection().sendPacket(d);
	}

	/*
	 * Expulsa a todos los usuarios del servidor
	 */
	@Override
	public void kickAll() {
		for (ServerUser user : getUsers()) {
			kick(user);
		}
		super.kickAll();
	}
	
	public void removeUser(ServerUser user) {
		users.remove(user);
		ui.statsBar.setUsers(countUsers());
	}

	public boolean addUser(ServerUser user) {
		if (countUsers() >= maxConnections) {
			// Avisa de que el servidor esta lleno y se le desconecta
			Disconnect d = new Disconnect("Server is full");
			user.getConnection().sendPacket(d);
			user.getConnection().close();
			return false;
		}
		users.add(user);
		new Thread(user).start();
		ui.statsBar.setUsers(countUsers());
		return false;
	}

	public static void main(String[] args) throws Exception {
		File actualDir = new File(".");
		// Comprobacion de escritura
		if (!actualDir.canWrite()) {
			throw new Exception("The application needs write permmissions.");
		}

		// Archivo para los logs ...
		FileHandler fh;
		try {
			fh = new FileHandler(dirName + "/" + JassapServer.class.getName()
					+ ".log");
			LOGGER.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		} catch (SecurityException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		}

		// Comprobaciones de ficheros de configuracion y directorios
		File config = new File(dirName);

		// Si el directorio no existe, se procede a crearlo
		if (!config.exists()) {
			if (!config.mkdir()) {
				LOGGER.severe("Directory: " + config.getAbsolutePath() + 
						" cannot be created.");
				throw new Exception("Directory: " + config.getAbsolutePath() + 
						" cannot be created.");
			}
		}

		// Fichero de salas
		File accounts = new File(dirName + "accounts.json");
		if (!accounts.exists()) {
			accounts.createNewFile();
		}

		// Fichero de salas
		File rooms = new File(dirName + "rooms.json");
		if (!rooms.exists()) {
			rooms.createNewFile();
		}

		// Fichero de configuracion del servidor
		File properties = new File(dirName + "jassap.properties");
		if (!properties.exists()) {
			properties.createNewFile();
		}

		LOGGER.info("Starting UI ...");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				ui = new ServerUI();
			}
		});

		// Carga config
		LOGGER.info("Loading server properties ...");
		serverProperties = new ServerProperties(properties);

		// Carga salas
		LOGGER.info("Loading rooms ...");
		roomDatabase = new RoomDatabase(rooms);
		LOGGER.info(roomDatabase.load() + " rooms loaded.");

		// Carga de usuarios
		LOGGER.info("Loading user accounts ...");
		accountDatabase = new AccountDatabase(accounts);
		LOGGER.info(accountDatabase.load() + " accounts loaded.");

		// Instancia servidor
		LOGGER.info("Starting server ... ");
		server = new JassapServer();
		server.rooms = roomDatabase.getRooms();
	}
}
