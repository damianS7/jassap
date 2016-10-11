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

import java.util.ArrayList;
import java.util.List;

import com.jassap.chat.Roles;
import com.jassap.chat.Room;
import com.jassap.database.Account;
import com.jassap.network.DataPacket;
import com.jassap.network.Packet;
import com.jassap.network.PacketHandler;
import com.jassap.network.User;
import com.jassap.network.packets.Ban;
import com.jassap.network.packets.ConversationMessage;
import com.jassap.network.packets.Disconnect;
import com.jassap.network.packets.Kick;
import com.jassap.network.packets.LoginRequest;
import com.jassap.network.packets.LoginResponse;
import com.jassap.network.packets.Mute;
import com.jassap.network.packets.RoomExit;
import com.jassap.network.packets.RoomJoinRequest;
import com.jassap.network.packets.RoomJoinResponse;
import com.jassap.network.packets.RoomMessage;
import com.jassap.network.packets.RoomsRequest;
import com.jassap.network.packets.RoomsResponse;

/**
 * Procesa los paquetes que el servidor recibe de los clientes y lo hace en
 * orden de llegada.
 * 
 * @author danjian
 */
public class ServerPacketHandler extends PacketHandler implements Runnable {
	protected boolean keepRunning = true;

	// Detiene el hilo que procesa los paquetes
	protected void stopRunning() {
		keepRunning = false;
	}

	@Override
	protected void handlePacket(DataPacket dp) {
		Packet p = dp.getPacket();

		System.out.println(p);

		if (p instanceof LoginRequest) {
			LoginRequest lr = (LoginRequest) p;
			Account acc = new Account(lr.getUsername(), lr.getPassword());
			Login login = new Login(acc);

			LoginResponse lrp = new LoginResponse(login.isValid(), login
					.getAccount().getRole());

			// Si los datos son invalidos desconectamos al cliente
			if (!lrp.isLogged()) {
				Disconnect d = new Disconnect("Invalid login credentials.");
				dp.getSender().sendPacket(d);
				dp.getSender().close();
				return;
			}

			ServerUser su = new ServerUser(dp.getSender(), acc);
			JassapServer.server.addUser(su);
			dp.getSender().sendPacket(lrp);

			return;
		}

		// El cliente pide la lista de salas disponibles
		if (p instanceof RoomsRequest) {
			List<String> roomsNames = new ArrayList<String>();

			for (Room room : JassapServer.server.getRooms()) {
				roomsNames.add(room.getName());
			}

			RoomsResponse rr = new RoomsResponse(roomsNames);
			dp.getSender().sendPacket(rr);
			return;
		}

		if (p instanceof RoomJoinRequest) {
			RoomJoinRequest join = (RoomJoinRequest) p;
			Room r = JassapServer.server.getRoom(join.getRoom());
			
			RoomJoinResponse rjr = new RoomJoinResponse(join.getRoom(),
			 r.getRoomUsernames());

			r.addUser(JassapServer.server.getUser(join.getUsername()));
			dp.getSender().sendPacket(rjr);
			return;
		}

		if (p instanceof RoomMessage) {
			RoomMessage rm = (RoomMessage) p;
			Room room = JassapServer.server.getRoom(rm.getRoom());
			room.difusion(rm.getSender(), rm.getMessage());
			return;
		}

		// from rhaegon to user0
		if (p instanceof ConversationMessage) {
			ConversationMessage cm = (ConversationMessage) p;
			User to = JassapServer.server.getUser(cm.getTo());
			
			if (to != null) {
				ConversationMessage cms = new ConversationMessage(cm.getFrom(),
						null, cm.getMessage());
				to.getConnection().sendPacket(cms);
			}
			
			dp.getSender().sendPacket(cm);
			return;
		}

		if(p instanceof RoomExit) {
			RoomExit re = (RoomExit) p;
			Room room = JassapServer.server.getRoom(re.getRoom());
			User u = JassapServer.server.getUser(re.getUser());
			room.removeUser(u);
			dp.getSender().sendPacket(re);
			return;
		}
		
		if(p instanceof Kick) {
			Kick k = (Kick) p;
			User mod = JassapServer.server.getUser(k.getBy());

			// No tiene privilegios
			if(mod.getAccount().getRole() == Roles.USER) {
				return;
			}
			
			User kicked = JassapServer.server.getUser(k.getTarget());
			Room room = JassapServer.server.getRoom(k.getRoom());

			// Si se encuentran el usuario y la sala ...
			if(kicked != null && room != null) {
				room.kick(kicked, k.getBy(), k.getTarget(), k.getReason());
			}
			
			return;
		}
		
		if(p instanceof Mute) {
			Mute m = (Mute) p;
			User mod = JassapServer.server.getUser(m.getBy());

			// No tiene privilegios
			if(mod.getAccount().getRole() == Roles.USER) {
				return;
			}
			
			User muted = JassapServer.server.getUser(m.getTarget());
			Room room = JassapServer.server.getRoom(m.getRoom());

			// Si se encuentran el usuario y la sala ...
			if(muted != null && room != null) {
				room.mute(m.getBy(), m.getTarget(), m.getReason(), m.getTime());
			}
			
			return;
		}
		
		if(p instanceof Ban) {
			Ban m = (Ban) p;
			User mod = JassapServer.server.getUser(m.getBy());

			// No tiene privilegios
			if(mod.getAccount().getRole() == Roles.USER) {
				return;
			}
			
			User muted = JassapServer.server.getUser(m.getTarget());
			Room room = JassapServer.server.getRoom(m.getRoom());

			// Si se encuentran el usuario y la sala ...
			if(muted != null && room != null) {
				room.ban(m.getBy(), m.getTarget(), m.getReason(), m.getTime());
			}
			
			return;
		}
		
		super.handlePacket(dp);
	}

	@Override
	public void run() {
		while (keepRunning) {
			// Si hay paquetes en cola ...
			if (!packetQueue.isEmpty()) {

				/*
				 * Iteramos sobre getQueuePackets() para no tener problemas con
				 * los hilos y el acceso a la lista, pues si trabamos
				 * directamente sobre ella al borrar puede haber problemas de
				 * concurrencia
				 */
				for (DataPacket packet : getQueuePackets()) {
					handlePacket(packet);
					packetQueue.remove(packet);
				}
			}
		}
	}
}
