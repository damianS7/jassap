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
package com.jassap.network.packets;

import com.jassap.chat.Roles;
import com.jassap.network.Packet;

/**
 * Este paquete es enviado del servidor al cliente en respuesta al paquete
 * @see LoginRequest
 * @author danjian
 */
public class LoginResponse extends Packet {
	private static final long serialVersionUID = -8124832437328062519L;
	private boolean logged = false;
	private Roles role = Roles.USER;
	
	public LoginResponse(boolean logged, Roles role) {
		this.logged = logged;
		this.role = role;
	}
	
	public boolean isLogged() {
		return logged;
	}
	
	public Roles getRole() {
		return role;
	}
}
