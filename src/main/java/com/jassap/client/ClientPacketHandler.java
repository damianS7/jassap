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
package com.jassap.client;

import com.jassap.client.ui.ConversationTab;
import com.jassap.client.ui.RoomTab;
import com.jassap.client.ui.TabChat;
import com.jassap.network.DataPacket;
import com.jassap.network.Packet;
import com.jassap.network.PacketHandler;
import com.jassap.network.packets.ConversationMessage;
import com.jassap.network.packets.Disconnect;
import com.jassap.network.packets.Kick;
import com.jassap.network.packets.LoginResponse;
import com.jassap.network.packets.RoomExit;
import com.jassap.network.packets.RoomJoinResponse;
import com.jassap.network.packets.RoomMessage;
import com.jassap.network.packets.RoomsResponse;

/**
 * Esta clase procesa los paquetes que recibe el cliente
 * @author danjian
 */
public class ClientPacketHandler extends PacketHandler {
	
	@Override
	protected void handlePacket(DataPacket dp) {
		Packet p = dp.getPacket();
		
		// Salida de una sala
		if (p instanceof RoomExit) {
			RoomExit re = (RoomExit) p;
			TabChat tc = JassapClient.ui.getTab(re.getRoom());
			JassapClient.ui.removeTab(tc);
			return;
		}
		
		// Conversacion
		if (p instanceof ConversationMessage) {
			ConversationMessage cm = (ConversationMessage) p;
			TabChat tc = JassapClient.ui.getTab(cm.getTo());
			
			if(tc instanceof ConversationTab) {
				ConversationTab ct = (ConversationTab) tc;
				ct.addText(cm.getFrom() + ":" + cm.getMessage());
			}
			return;
		}
		
		// Respuesta a una peticion de login
		if (p instanceof LoginResponse) {
			LoginResponse lr = (LoginResponse) p;
			JassapClient.client.getClientUser().getAccount().setRole(lr.getRole());
			return;
		}
		
		// El usuario fue kickeado
		if (p instanceof Kick) {
			Kick k = (Kick) p;
			TabChat tc = JassapClient.ui.getTab(k.getRoom());
			if(tc instanceof RoomTab) {
				RoomTab rt = (RoomTab) tc;
				rt.addText("Kicked by: " + k.getKickedBy() + ". reason: " + 
				k.getReason());
			}
			return;
		}
		
		// Un nuevo mensaje para la sala
		if (p instanceof RoomMessage) {
			RoomMessage rm = (RoomMessage) p;
			TabChat tc = JassapClient.ui.getTab(rm.getRoom());
			
			if(tc instanceof RoomTab) {
				RoomTab rt = (RoomTab) tc;
				rt.addText(rm.getSender() + ":" + rm.getMessage());
			}
			return;
		}
		
		// Informacion de salas salas disponibles
		if (p instanceof RoomsResponse) {
			RoomsResponse rr = (RoomsResponse) p;
			JassapClient.ui.rooms.addRooms(rr.getRooms());
			return;
		}
		
		// Usuarios que componen la sala, mods de la sala ...
		if (p instanceof RoomJoinResponse) {
			RoomJoinResponse rd = (RoomJoinResponse) p;
			RoomTab rt = new RoomTab(rd.getRoom());
			rt.addUsers(rd.getUsers());
			JassapClient.ui.addTab(rt);
			return;
		}
		
		if (p instanceof Disconnect) {
			JassapClient.client.disconnect();
			Disconnect d = (Disconnect) p;
			JassapClient.ui.log.addText("Disconnected by Server. reason: " + d.getReason());
		}
		
		super.handlePacket(dp);
	}
}
