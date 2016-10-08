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

import java.io.Serializable;

/**
 * Esta clase es la base de todos los paquetes usados en la comunicacion entre
 * el servidor y el cliente. A partir de esta clase todos los nuevos paquetes
 * se crean a partir de esta. 
 * @author danjian
 */
public abstract class Packet implements Serializable {
	private static final long serialVersionUID = 4858010588787649345L;
}
