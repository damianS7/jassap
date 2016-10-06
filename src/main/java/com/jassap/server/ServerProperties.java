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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Simplifica la carga de configuracion del servidor
 * 
 * @author danjian
 */
public class ServerProperties extends Properties {
	private static final long serialVersionUID = -6394526361262062013L;
	private boolean modyfied = false;
	private File file;
	private String[][] fields = { { "username", "Rhaegon" },
			{ "password", "123456" }, { "server_ip", "127.0.0.1" },
			{ "server_port", "7777" }, 
			{ "max_connections", "200" } };

	public ServerProperties(File file) throws IOException {
		this.file = file;
		load(new FileInputStream(file));

		// Comprueba que las claves obligatorias estan en el fichero de
		// configuracion
		for (String v[] : fields) {
			// Clave no encontrada
			if (getProperty(v[0]) == null) {
				setProperty(v[0], v[1]);
				modyfied = true;
			}
		}
		
		// Si faltaban campos se guarda el fichero
		if(modyfied) {
			save();
		}
	}

	// Guarda en un fichero
	public void save() throws IOException {
		store(new FileOutputStream(file), "Server properties");
	}

	public String getAddress() {
		return getProperty("server_ip");
	}

	public void setAddress(String address) {
		setProperty("server_ip", address);
	}

	public int getPort() {
		return Integer.parseInt(getProperty("server_port"));
	}

	public void setPort(int port) {
		setProperty("server_port", Integer.toString(port));
	}
	
	public void setPhone(String phone) {
		setProperty("phonenumber", phone);
	}
	
	public void setPassword(String password) {
		setProperty("password", password);
	}
	
	public String getPassword() {
		return getProperty("password"); 
	}
	
	public String getPhone() {
		return getProperty("phonenumber");
	}
	
	public String getAvatarPath() {
		return getProperty("avatar_path");
	}
	
	public void setAvatarPath(String path) {
		setProperty("avatar_path", path);
	}
	
	public boolean getSaveLogs() {
		return Boolean.parseBoolean(getProperty("save_logs"));
	}
	
	public void setSaveLogs(String bool) {
		setProperty("save_logs", bool);
	}
	
	public String getUsername() {
		return getProperty("username");
	}
	
	public void setUsername(String username) {
		setProperty("username", username);
	}
}
